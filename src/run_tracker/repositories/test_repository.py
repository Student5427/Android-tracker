from repositories.base import BaseRepository


class TestRepository(BaseRepository):
    # TODO: delete
    async def test_method(self) -> int:
        return 1