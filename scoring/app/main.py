"""
Микросервис кредитного скоринга на FastAPI + Kafka.
Коммуникация с Java-микросервисом:
- Java отправляет сообщения в Kafka topic "credit.requests"
- Python читает, вычисляет вероятность дефолта, отправляет ответ в "credit.responses"
- Дополнительно: REST endpoint /score для синхронных запросов (удобно для тестов)
"""

import asyncio
import json
from contextlib import asynccontextmanager
from fastapi import FastAPI, HTTPException
from aiokafka import AIOKafkaConsumer, AIOKafkaProducer
from .schemas import ScoreRequest, ScoreResponse
from .model import predict

# === Конфигурация Kafka (переопределяется через env) ===
import os
KAFKA_BOOTSTRAP = os.getenv("KAFKA_BOOTSTRAP", "localhost:9092")
REQUEST_TOPIC = "credit.requests"
RESPONSE_TOPIC = "credit.responses"
GROUP_ID = "python-scoring-group"

# Глобальные объекты consumer/producer
consumer = None
producer = None

# === Lifespan для FastAPI: старт/стоп Kafka компонентов ===
@asynccontextmanager
async def lifespan(app: FastAPI):
    global consumer, producer
    # Инициализация Kafka consumer и producer
    consumer = AIOKafkaConsumer(
        REQUEST_TOPIC,
        bootstrap_servers=KAFKA_BOOTSTRAP,
        group_id=GROUP_ID,
        value_deserializer=lambda m: json.loads(m.decode("utf-8")),
        auto_offset_reset="earliest",
        enable_auto_commit=True
    )
    producer = AIOKafkaProducer(
        bootstrap_servers=KAFKA_BOOTSTRAP,
        value_serializer=lambda v: json.dumps(v).encode("utf-8")
    )
    await consumer.start()
    await producer.start()
    print(f"Kafka connected to {KAFKA_BOOTSTRAP}")
    # Запускаем фоновую задачу обработки сообщений из Kafka
    asyncio.create_task(kafka_loop())
    yield
    # Очистка при выключении
    await consumer.stop()
    await producer.stop()
    print("Kafka connections closed")

# === Фоновая задача: читает запросы из Kafka, вызывает predict, отправляет ответ ===
async def kafka_loop():
    print("Kafka consumer loop started, waiting for messages...")
    try:
        async for msg in consumer:
            request_data = msg.value
            request_id = request_data.get("request_id")
            # Валидация входных данных
            try:
                req = ScoreRequest(**request_data)
                prob = predict([req.age, req.income, req.loan_amount, req.credit_history])
                decision = "approve" if prob < 0.5 else "reject"
                response = ScoreResponse(
                    request_id=request_id,
                    probability=prob,
                    decision=decision
                )
            except Exception as e:
                # В случае ошибки отправляем ответ с ошибкой
                response = ScoreResponse(
                    request_id=request_id,
                    probability=0.0,
                    decision="error",
                    error=str(e)
                )
            # Отправка ответа в топик
            await producer.send(RESPONSE_TOPIC, value=response.dict())
            print(f"Processed {request_id} -> {decision} (prob={response.probability:.3f})")
    except Exception as e:
        print(f"Kafka loop error: {e}")

# === FastAPI приложение ===
app = FastAPI(
    title="Credit Scoring Microservice",
    description="Predicts default probability using logistic regression. "
                "Listens to Kafka topic 'credit.requests' and sends responses to 'credit.responses'. "
                "Also provides REST endpoint /score for testing.",
    version="1.0",
    lifespan=lifespan
)

@app.get("/health")
async def health_check():
    """Проверка жизнеспособности сервиса"""
    return {"status": "alive", "kafka_connected": consumer is not None and not consumer._closed}

@app.post("/score", response_model=ScoreResponse)
async def score_sync(request: ScoreRequest):
    """
    Синхронный REST endpoint для скоринга (без Kafka).
    Полезен для отладки и интеграций, не требующих асинхронности.
    """
    try:
        prob = predict([request.age, request.income, request.loan_amount, request.credit_history])
        decision = "approve" if prob < 0.5 else "reject"
        return ScoreResponse(
            request_id=request.request_id,
            probability=prob,
            decision=decision
        )
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

# === Точка входа при запуске скрипта ===
if __name__ == "__main__":
    import uvicorn
    # Для локального запуска (без Docker):
    uvicorn.run("app.main:app", host="0.0.0.0", port=8000, reload=True)