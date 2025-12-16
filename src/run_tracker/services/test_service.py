from fastapi import Depends
from sqlalchemy.ext.asyncio import AsyncSession
from core.database import get_db
from run_tracker.repositories.test_repository import TestRepository


class TestService:
    def __init__(
        self,
        session: AsyncSession = Depends(get_db),
    ) -> None:
        self.repository = TestRepository(session)

    async def test_method(self) -> int:
        return await self.repository.test_method()