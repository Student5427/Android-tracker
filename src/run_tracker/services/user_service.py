from uuid import UUID

from fastapi import Depends
from sqlalchemy.ext.asyncio import AsyncSession

from core.database import get_session
from enums.error_code import ErrorCodeEnum
from exceptions.base import ValidationException
from run_tracker.exceptions.user import UserNotFoundException
from run_tracker.models import User
from run_tracker.repositories.user_repository import UserRepository
from run_tracker.schemas.user import UserCreateSchema, UserOutSchema, UserUpdateSchema


class UserService:
    def __init__(self, session: AsyncSession = Depends(get_session)) -> None:
        self.repository = UserRepository(session)

    async def create_user(self, payload: UserCreateSchema) -> UUID:
        # TODO: add validations

        user = await self.repository.create_user(payload)

        return user.id

    async def get_user_by_id_or_raise_404(self, user_id: UUID) -> User:
        user = await self.repository.get_user_by_id(user_id)
        if not user:
            raise UserNotFoundException(id=user_id)

        return user

    @staticmethod
    def get_user_out_schema(user: User) -> UserOutSchema:
        return UserOutSchema(
            id=user.id,
            first_name=user.first_name,
            last_name=user.last_name,
            patronymic=user.patronymic,
            email=user.email,
            weight=user.weight,
            height=user.height,
            created=user.created,
            created_by=user.created_by,
            updated=user.updated,
            updated_by=user.updated_by,
            is_deleted=user.is_deleted,
            deleted=user.deleted,
            deleted_by=user.deleted_by,
        )

    async def get_user_by_id(self, user_id: UUID) -> UserOutSchema:
        user = await self.get_user_by_id_or_raise_404(user_id)

        return self.get_user_out_schema(user)

    async def update_user(self, user_id: UUID, payload: UserUpdateSchema) -> UserOutSchema:
        user = await self.get_user_by_id_or_raise_404(user_id)

        if user.is_deleted:
            raise ValidationException(
                field="is_deleted",
                code=ErrorCodeEnum.RECORD_DELETED.value,
                message="Cannot update a deleted record, recover it first",
            )

        # TODO: add validations

        user = await self.repository.update_user(user, payload)
        return self.get_user_out_schema(user)