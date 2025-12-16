from fastapi import FastAPI
import uvicorn
from settings import settings
from run_tracker.api import api as run_tracker_api
from models.schemas import UserCreate, UserResponse
from models.user import User
from core.database import get_db
from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.exc import IntegrityError


app = FastAPI(
    title="Run tracker",
    description="API for mobile run tracker app",
    version="1.0.0",
)


app.include_router(run_tracker_api.router, prefix="/api")


@app.post("/register")
async def register_user(
    user: UserCreate,
    db: AsyncSession = Depends(get_db),
):
    new_user = User(**user.model_dump())

    db.add(new_user)

    try:
        await db.commit()
        await db.refresh(new_user)
    except IntegrityError:
        await db.rollback()
        raise HTTPException(
            status_code=409,
            detail="Email уже существует",
        )

    return new_user

@app.get("/")
def read_root():
    return {"message": "Welcome to FastAPI"}

@app.get("/health")
def health_check():
    return {"status": "healthy"}

@app.get("/health")
def health_check():
    return {"status": "healthy"}

if __name__ == "__main__":
    uvicorn.run(app, host="localhost", port=settings.API_SERVICE_PORT)