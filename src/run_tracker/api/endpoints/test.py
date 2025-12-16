from fastapi import APIRouter, Depends

from run_tracker.services.test_service import TestService


router = APIRouter()


@router.get("/", response_model=int)
async def get_test(service: TestService = Depends()) -> int:
    """
    Тестовый эндпоинт
    """
    return await service.test_method()