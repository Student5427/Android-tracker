from sqlalchemy.ext.asyncio import async_sessionmaker, create_async_engine
from asyncio import shield
from settings import settings


async_engine = create_async_engine(
    url=settings.db_url,
    max_overflow=settings.DB_ENGINE_MAX_OVERFLOW,
    pool_pre_ping=settings.DB_ENGINE_POOL_PRE_PING,
    pool_recycle=settings.DB_ENGINE_POOL_RECYCLE,
    pool_size=settings.DB_ENGINE_POOL_SIZE,
    pool_timeout=settings.DB_ENGINE_POOL_TIMEOUT,
)

create_async_session = async_sessionmaker(async_engine, expire_on_commit=False)


async def get_session():
    session = create_async_session()
    try:
        yield session
        await session.commit()
    except Exception:
        await session.rollback()
        raise
    finally:
        if session:
            await shield(session.close())