from uuid import UUID

from sqlalchemy import select

from repositories.base import BaseRepository
from run_tracker.models import User


class UserRepository(BaseRepository):
    async def get_user_by_id(self, user_id: UUID) -> User:
        statement = select(User).where(User.id == user_id)

        return (await self.session.execute(statement)).scalars().unique().one_or_none()