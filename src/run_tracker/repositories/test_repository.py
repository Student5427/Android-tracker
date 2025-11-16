from repositories.base import BaseRepository


class TestRepository(BaseRepository):
    async def test_method(self) -> int:
        return 1