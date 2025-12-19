from fastapi import APIRouter

from run_tracker.api.endpoints.user import router as user_router
from run_tracker.api.endpoints.user_training import router as user_training_router

router = APIRouter()

router.include_router(user_router, prefix="/users", tags=["users"])
router.include_router(user_training_router, prefix="/users/{user_id}/trainings", tags=["user-trainings"])