from uuid import UUID

from fastapi import APIRouter, Depends

from run_tracker.schemas.user import UserCreateSchema, UserOutSchema, UserUpdateSchema
from run_tracker.services.user_service import UserService


router = APIRouter()


@router.post("/", response_model=UUID)
async def create_user(payload: UserCreateSchema, service: UserService = Depends()) -> UUID:
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


@router.put("/{user_id}/", response_model=UserOutSchema)
async def update_user(user_id: UUID, payload: UserUpdateSchema, service: UserService = Depends()) -> UserOutSchema:
    """
    Обновление данных пользователя
    """
    return await service.update_user(user_id, payload)