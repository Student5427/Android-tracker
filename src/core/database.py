from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession, async_sessionmaker
from sqlalchemy.orm import sessionmaker, mapped_column, declarative_base
from sqlalchemy.orm import DeclarativeBase, mapped_column, Mapped
from sqlalchemy.types import BigInteger



class Base(DeclarativeBase):
    id : Mapped[BigInteger] = mapped_column(BigInteger, primary_key=True)


# Формат подключения: postgresql+asyncpg://user:password@host:port/dbname
DATABASE_URL = "postgresql+asyncpg://postgres:postgres@postgres:5432/run_tracker"

# Асинхронный движок SQLAlchemy
engine = create_async_engine(DATABASE_URL, echo=True)

# Асинхронная сессия для создания новых по мере необходимости
async_session_maker = sessionmaker(
    bind=engine,
    class_=AsyncSession,
    expire_on_commit=False,
)

# Base = declarative_base()

async def get_db():
    async with async_session_maker() as session:
        yield session




# Декоратор для асинхронного подключения