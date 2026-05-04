import pickle
import numpy as np
import os

MODEL_PATH = os.path.join(os.path.dirname(__file__), "..", "model.pkl")
SCALER_PATH = os.path.join(os.path.dirname(__file__), "..", "scaler.pkl")

_model = None
_scaler = None

def load_model():
    #загружает модель и scaler один раз при старте сервиса
    global _model, _scaler
    if _model is None:
        with open(MODEL_PATH, "rb") as f:
            _model = pickle.load(f)
        with open(SCALER_PATH, "rb") as f:
            _scaler = pickle.load(f)
        print("Model and scaler loaded successfully")
    return _model, _scaler

def predict(features: list) -> float:
    #принимает список
    model, scaler = load_model()
    # Преобразуем в numpy массив и масштабируем
    X = np.array(features).reshape(1, -1)
    X_scaled = scaler.transform(X)
    prob = model.predict_proba(X_scaled)[0, 1]   # вероятность класса "дефолт"
    return float(prob)