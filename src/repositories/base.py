import abc
from typing import Any, Generic, Iterable, List, Mapping, Optional, Sequence, Tuple, TypeVar

from fastapi_pagination import Page
from fastapi_pagination.ext.sqlalchemy import paginate
from sqlalchemy import (
    Insert,
    Result,
    Row,
    TextClause,
)
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.sql import Delete, Select, Update
from sqlalchemy.sql.dml import ReturningDelete, ReturningUpdate

T = TypeVar("T")


class BaseRepository(abc.ABC, Generic[T]):
    def __init__(self, session: AsyncSession):
        self.session = session

    async def save(self, obj: T) -> T:
        self.session.add(obj)
        await self.session.flush()
        await self.session.refresh(obj)
        return obj

    async def flush(self) -> None:
        await self.session.flush()

    async def save_all(self, objs: list[T]) -> None:
        self.session.add_all(objs)

    async def one_or_none(self, statement: Select) -> Optional[Result]:
        return (await self.session.execute(statement)).scalars().one_or_none()

    async def one_or_none_unique(self, statement: Select) -> Optional[Result]:
        return (await self.session.execute(statement)).scalars().unique().one_or_none()

    async def one(self, statement: Select) -> Any:
        return (await self.session.execute(statement)).scalars().one()

    async def scalar_all(self, statement: Select | ReturningUpdate | ReturningDelete) -> Iterable[Any]:
        return (await self.session.execute(statement)).scalars().all()

    async def all(self, statement: Select) -> Sequence[Row[Tuple[Any, ...]]]:
        return (await self.session.execute(statement)).all()

    async def execute(self, statement: Select | Update | Delete | Insert) -> Result[Any]:
        return await self.session.execute(statement)

    async def all_unique(self, statement: Select) -> Iterable[Any]:
        result = await self.session.execute(statement)
        return result.scalars().unique().all()

    async def first(self, statement: Select) -> Optional[Result]:
        return (await self.session.execute(statement)).scalars().first()

    async def paginate(self, statement: Select, **kwargs: Any) -> Page:
        return await paginate(self.session, statement, **kwargs)

    async def remove(self, obj: T) -> None:
        return await self.session.delete(obj)

    async def fetch_mapped_results(self, statement: Select) -> List[Mapping[str, Any]]:
        result = await self.session.execute(statement)
        return list(result.mappings())

    async def execute_raw_sql(self, sql: TextClause) -> Result[Any]:
        return await self.session.execute(sql)

    async def init_session_connection(self) -> None:
        await self.session.connection()

    async def refresh(self, obj: T) -> None:
        await self.session.refresh(obj)
