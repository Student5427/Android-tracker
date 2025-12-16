from fastapi import APIRouter

from run_tracker.api.endpoints.test import router as test_router


router = APIRouter()

router.include_router(test_router, prefix="/test", tags=["test"])