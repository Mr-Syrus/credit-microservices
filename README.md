# credit-microservices - Microservice Backend with Credit Scoring Model Based on Logistic Regression

The project is a system for processing credit applications with asynchronous scoring via Apache Kafka.  
It consists of two microservices:

- **Java (Spring Boot)** – user management, application creation, sending events to Kafka, receiving scoring results.
- **Python (FastAPI + aiokafka)** – credit scoring model training (logistic regression), default probability prediction, processing messages from Kafka.

## Architecture

1. The user (client) creates an application via the REST API of the Java service.
2. Java saves the application to the database and sends an event to the Kafka topic `scoring-requests`.
3. The Python microservice reads the topic, calculates the default probability (based on age, income, amount, marital status, loan term), and sends the result to the `credit.responses` topic.
4. The Java consumer receives the result, saves it to the `scoringEntity` table, and updates the application status in `applicationEntity` (APPROVED / REJECTED).

## Requirements

- **Docker** and **Docker Compose** (for Kafka)
- **Java 17+** and Gradle
- **Python 3.14+** (using a virtual environment is recommended)
- **PostgreSQL** (for Java service data storage) – can also be run via Docker

## Infrastructure Setup

1. Initialize dependencies for both services and create a virtual environment.
2. Train the model: `credit-microservices/scoring/train_model.py`
3. Start the database.
4. Start `Kafka` (docker-compose).
5. Start the `scoring` microservice.
6. Start the `api` microservice.

## Testing

- For the Java microservice (`api`), Swagger documentation is available.
- For the Python microservice (`scoring`), a separate `/health` endpoint is provided, which allows you to verify that the service is running and connected to Kafka.
