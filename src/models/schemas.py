from pydantic import BaseModel
from typing import Optional

class UserCreate(BaseModel):
    name: str
    email: str
    weight: Optional[float] = None
    height: Optional[float] = None

class UserResponse(UserCreate):
    id: int

    class Config:
        from_attributes = True