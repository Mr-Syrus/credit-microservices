import pickle
import numpy as np
import os

# Пути к файлам (лежат в корневой папке python-scoring)
MODEL_PATH = os.path.join(os.path.dirname(__file__), "..", "model.pkl")
SCALER_PATH = os.path.join(os.path.dirname(__file__), "..", "scaler.pkl")

# Глобальные переменные для модели и scaler
_model = None
_scaler = None

def load_model():
    """Загружает модель и scaler один раз при старте сервиса"""
    global _model, _scaler
    if _model is None:
        with open(MODEL_PATH, "rb") as f:
            _model = pickle.load(f)
        with open(SCALER_PATH, "rb") as f:
            _scaler = pickle.load(f)
        print("Model and scaler loaded successfully")
    return _model, _scaler

def predict(features: list) -> float:
    """
    Принимает список [age, income, loan_amount, credit_history]
    Возвращает вероятность дефолта (0..1)
    """
    model, scaler = load_model()
    # Преобразуем в numpy массив и масштабируем
    X = np.array(features).reshape(1, -1)
    X_scaled = scaler.transform(X)
    prob = model.predict_proba(X_scaled)[0, 1]   # вероятность класса "дефолт" (1)
    return float(prob)

# Для демонстрации: если файлов model.pkl/scaler.pkl нет,
# можно обучить простую модель прямо здесь.
# Раскомментируйте при необходимости.
"""
def train_dummy_model():
    from sklearn.linear_model import LogisticRegression
    from sklearn.preprocessing import StandardScaler
    import numpy as np
    # Генерация 1000 синтетических клиентов
    np.random.seed(42)
    X = np.random.rand(1000, 4)
    # Простая линейная зависимость: риск выше при большем loan_amount и credit_history
    y = (X[:, 2] * 0.8 + X[:, 3] * 1.2 > 0.6).astype(int)
    scaler = StandardScaler().fit(X)
    model = LogisticRegression().fit(scaler.transform(X), y)
    with open("model.pkl", "wb") as f: pickle.dump(model, f)
    with open("scaler.pkl", "wb") as f: pickle.dump(scaler, f)
    print("Dummy model trained and saved")
# Вызвать train_dummy_model() если нет готовой модели
"""