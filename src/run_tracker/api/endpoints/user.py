from uuid import UUID

from fastapi import APIRouter, Depends, status

from run_tracker.schemas.user import UserCreateSchema, UserOutSchema
from run_tracker.services.user_service import UserService


router = APIRouter()


@router.post("/", status_code=status.HTTP_200_OK)
async def create_user(payload: UserCreateSchema, service: UserService = Depends()) -> None:
    """
    Регистрация нового пользователя
    """
    return await service.create_user(payload)


@router.get("/{user_id}/", response_model=UserOutSchema)
async def get_user_by_id(user_id: UUID, service: UserService = Depends()) -> UserOutSchema:
    """
    Получение пользователя по uuid
    """
    return await service.get_user_by_id(user_id)