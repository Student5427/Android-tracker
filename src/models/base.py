from datetime import datetime
from typing import ClassVar
from uuid import UUID

from sqlalchemy import UUID as DB_UUID, Boolean, DateTime, FetchedValue, MetaData, func
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column
from sqlalchemy.sql import expression

psql_convention = {
    "ix": "ix_%(column_0_label)s",
    "uq": "uq_%(table_name)s_%(column_0_name)s",
    "ck": "ck_%(table_name)s_%(column_0_name)s",
    "fk": "fk_%(table_name)s_%(column_0_name)s_%(referred_table_name)s",
    "pk": "pk_%(table_name)s",
}
psql_metadata = MetaData(naming_convention=psql_convention)


class Base(DeclarativeBase):
    metadata = psql_metadata

    __allow_unmapped__ = True
    __mapper_args__ = {"eager_defaults": True}

    __repr_cols_num = 3
    __repr_cols: ClassVar[tuple[str, ...]] = tuple()

    def __repr__(self) -> str:
        cols = []
        for idx, col in enumerate(self.__table__.columns.keys()):
            if col in self.__repr_cols or idx < self.__repr_cols_num:
                cols.append(f"{col}={getattr(self, col)}")

        return f"<{self.__class__.__name__} {', '.join(cols)}>"


class BaseWithCreateAndUpdateTime(Base):
    __abstract__ = True

    created: Mapped[datetime] = mapped_column(DateTime(timezone=True), server_default=func.now())
    updated: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        default=func.now(),
        onupdate=func.now(),
        server_default=func.now(),
        server_onupdate=FetchedValue(),
    )
    created_by: Mapped[UUID] = mapped_column(nullable=True)
    updated_by: Mapped[UUID] = mapped_column(nullable=True)


class BaseWithDelete(Base):
    __abstract__ = True

    is_deleted: Mapped[bool] = mapped_column(Boolean, server_default=expression.false())
    deleted: Mapped[datetime] = mapped_column(DateTime(timezone=True), nullable=True)
    deleted_by: Mapped[UUID] = mapped_column(DB_UUID, nullable=True)
