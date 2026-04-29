from pydantic import BaseModel
from typing import Optional

class ScoreRequest(BaseModel):
    request_id: Optional[str] = None  
    age: int
    income: int
    loan_amount: int
    credit_history: int

class ScoreResponse(BaseModel):
    request_id: Optional[str] = None
    probability: float
    decision: str
    error: Optional[str] = None