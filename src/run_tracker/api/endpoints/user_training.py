from uuid import UUID

from fastapi import APIRouter, Depends
from fastapi_pagination import Page

from run_tracker.schemas.user_training import (
    UserTrainingCreateSchema,
    UserTrainingUpdateSchema,
    UserTrainingListOutSchema,
    UserTrainingOutSchema,
)
from run_tracker.services.user_training_service import UserTrainingService

router = APIRouter()


@router.post("/", response_model=int)
async def create_user_training(
    user_id: UUID, payload: UserTrainingCreateSchema, service: UserTrainingService = Depends()
) -> int:
    """
    Создание новой тренировки пользователя
    """
    return await service.create_user_training(user_id, payload)


@router.get("/", response_model=Page[UserTrainingListOutSchema])
async def get_user_trainings(
    user_id: UUID, service: UserTrainingService = Depends()  # TODO: add filtering and sorting
) -> Page[UserTrainingListOutSchema]:
    """
    Получение тренировок пользователя с пагинацией
    """
    return await service.get_user_trainings(user_id)


@router.get("/{training_id}/", response_model=UserTrainingOutSchema)
async def get_user_training_by_id(
    user_id: UUID, training_id: int, service: UserTrainingService = Depends()
) -> UserTrainingOutSchema:
    """
    Получение тренировки пользователя по id
    """
    return await service.get_user_training_by_id(user_id, training_id)


@router.put("/{training_id}/", response_model=UserTrainingOutSchema)
async def update_user_training(
    user_id: UUID, training_id: int, payload: UserTrainingUpdateSchema, service: UserTrainingService = Depends()
) -> UserTrainingOutSchema:
    """
    Обновление данных тренировки пользователя
    """
    return await service.update_user_training(user_id, training_id, payload)
