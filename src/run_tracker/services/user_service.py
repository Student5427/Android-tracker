from uuid import UUID

from fastapi import Depends
from sqlalchemy.ext.asyncio import AsyncSession
from core.database import get_session
from run_tracker.exceptions.user import UserNotFoundException
from run_tracker.models import User
from run_tracker.repositories.user_repository import UserRepository
from run_tracker.schemas.user import UserCreateSchema, UserOutSchema


class UserService:
    def __init__(self, session: AsyncSession = Depends(get_session)) -> None:
        self.repository = UserRepository(session)

    async def create_user(self, payload: UserCreateSchema) -> None:
        user = User(
            first_name=payload.first_name,
            last_name=payload.last_name,
            patronymic=payload.patronymic,
            email=payload.email,
            weight=payload.weight,
            height=payload.height,
        )

        await self.repository.save(user)

    async def get_user_by_id_or_raise_404(self, user_id: UUID) -> User:
        user = await self.repository.get_user_by_id(user_id)
        if not user:
            raise UserNotFoundException(id=user_id)

        return user

    async def get_user_by_id(self, user_id: UUID) -> UserOutSchema:
        user = await self.get_user_by_id_or_raise_404(user_id)

        return UserOutSchema(
            id=user.id,
            first_name=user.first_name,
            last_name=user.last_name,
            patronymic=user.patronymic,
            email=user.email,
            weight=user.weight,
            height=user.height,
        )