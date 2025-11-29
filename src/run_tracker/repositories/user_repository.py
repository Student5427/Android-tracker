from uuid import UUID

from sqlalchemy import select

from repositories.base import BaseRepository
from run_tracker.models import User
from run_tracker.schemas.user import UserCreateSchema, UserUpdateSchema


class UserRepository(BaseRepository):
    async def create_user(self, payload: UserCreateSchema) -> User:
        user = User(
            first_name=payload.first_name,
            last_name=payload.last_name,
            patronymic=payload.patronymic,
            email=payload.email,
            weight=payload.weight,
            height=payload.height,
        )

        return await self.save(user)

    async def get_user_by_id(self, user_id: UUID) -> User:
        statement = select(User).where(User.id == user_id)

        return (await self.session.execute(statement)).scalars().unique().one_or_none()

    async def update_user(self, user: User, payload: UserUpdateSchema) -> User:
        user.first_name = payload.first_name
        user.last_name = payload.last_name
        user.patronymic = payload.patronymic
        user.email = payload.email
        user.weight = payload.weight
        user.height = payload.height

        return await self.save(user)