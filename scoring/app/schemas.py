from pydantic import BaseModel
from typing import Optional

class ScoreRequest(BaseModel):
    applicationId: int
    age: int
    monthlyIncome: float
    creditAmount: float
    materialStatus: bool
    creditTermMonths: int

class ScoreResponse(BaseModel):
    applicationId: int
    probability: float
    decision: str
    error: Optional[str] = None