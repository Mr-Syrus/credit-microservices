from pydantic import BaseModel
from typing import Optional

class ScoreRequest(BaseModel):
    age: int
    monthlyIncome: float
    creditAmount: float
    materialStatus: bool
    creditTermMonths: int

class ScoreResponse(BaseModel):
    request_id: Optional[str] = None
    probability: float
    decision: str
    error: Optional[str] = None