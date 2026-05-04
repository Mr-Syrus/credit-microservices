# Скрипт для обучения модели кредитного скоринга (логистическая регрессия).
# Результат: model.pkl и scaler.pkl.

import numpy as np
import pandas as pd
import pickle
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, roc_auc_score, classification_report

np.random.seed(42)

n_samples = 10000

# признаки, генерация данных
age = np.random.randint(18, 70, n_samples) # возраст
income = np.random.randint(300, 3000, n_samples) # доход в тыс. руб
loan_amount = np.random.randint(50, 500, n_samples) # сумма кредита в тыс. руб
credit_term_months = np.random.randint(6, 60, n_samples) # срок кредита в месяцах
marital_status = np.random.choice([0, 1], n_samples, p=[0.4, 0.6]) # состоит в браке

X = np.column_stack((age, income, loan_amount, marital_status))

# целевая переменная y (дефолт = 1, возврат = 0)
# формируем логику дефолта, вероятность дефолта вычисляется как сигмоида от линейной комбинации
linear = (-0.03 * age
          - 0.0005 * income
          + 0.02 * loan_amount
          - 0.6 * marital_status
          + 0.01 * credit_term_months
          + 0.5) # свободный член (intercept)

# применяем сигмоиду, чтобы получить вероятность дефолта
prob_default = 1 / (1 + np.exp(-linear))

# генерируем бинарные метки: дефолт случается с вероятностью prob_default
y = (np.random.random(n_samples) < prob_default).astype(int)

print(f"Доля дефолтов в сгенерированных данных: {y.mean():.3f}")

# разделение на обучающую и тестовую выборки
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, stratify=y, random_state=42)

# масштабирование признаков (StandardScaler)
# логистическая регрессия чувствительна к масштабу, поэтому нормализуем
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# обучение логистической регрессии
# регуляризация L2 по умолчанию
model = LogisticRegression(C=1.0, max_iter=1000, random_state=42)
model.fit(X_train_scaled, y_train)

# оценка качества на тестовой выборке
y_pred = model.predict(X_test_scaled)
y_pred_proba = model.predict_proba(X_test_scaled)[:, 1]

accuracy = accuracy_score(y_test, y_pred)
roc_auc = roc_auc_score(y_test, y_pred_proba)

print(f"Accuracy на тесте: {accuracy:.4f}")
print(f"ROC-AUC на тесте: {roc_auc:.4f}")
print("\nClassification report:")
print(classification_report(y_test, y_pred, target_names=['returned', 'default']))

# интерпретация коэффициентов (важность признаков)
feature_names = ['age', 'income', 'loan_amount', 'credit_history']
coefficients = model.coef_[0]
print("\nКоэффициенты модели (влияние на логарифм шансов дефолта):")
for name, coef in zip(feature_names, coefficients):
    print(f"  {name}: {coef:.4f}")

# сохранение модели и scaler в файлы для использования в микросервисе
with open('model.pkl', 'wb') as f:
    pickle.dump(model, f)
with open('scaler.pkl', 'wb') as f:
    pickle.dump(scaler, f)

print("\nМодель сохранена в 'model.pkl', scaler сохранён в 'scaler.pkl'.")