from datetime import datetime

from sqlalchemy import String, Numeric, DateTime, func
from sqlalchemy.orm import Mapped, mapped_column

from core.database import Base


class User(Base):
    __tablename__ = "users"

    id: Mapped[int] = mapped_column(primary_key=True, index=True)

    name: Mapped[str] = mapped_column(String(100), nullable=False)

    email: Mapped[str] = mapped_column(
        String(255),
        unique=True,
        nullable=False,
        index=True
    )

    weight: Mapped[float | None] = mapped_column(Numeric(5, 2))
    height: Mapped[float | None] = mapped_column(Numeric(5, 2))

    created_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        server_default=func.now()
    )

# from sqlalchemy import Column, Integer, String, Date, BigInteger
# from core.database import Base
# from sqlalchemy.ext.declarative import declarative_base
# from sqlalchemy.orm import mapped_column, DeclarativeBase, Mapped

# class FileDB(Base):
#     __tablename__ = "upload_files"
    
#     # id: Mapped[int] = mapped_column(BigInteger, primary_key=True, index=True)
#     filename: Mapped[str] = mapped_column(String(150), nullable=False)
#     date: Mapped[Date] = mapped_column(Date, nullable=False)
#     upload_by: Mapped[str] = mapped_column(String(50), nullable=False)
#     # Другие поля, которые хранятся в БД