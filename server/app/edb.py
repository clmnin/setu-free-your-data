from typing import AsyncGenerator

from edgedb import AsyncIOConnection, AsyncIOPool, create_async_pool

from app.middleware.config import settings

pool: AsyncIOPool


async def edb_create_pool() -> None:
    global pool
    pool = await create_async_pool(
        dsn=settings.EDGEDB_DB
    )


async def edb_close_pool() -> None:
    await pool.aclose()


async def get_con() -> AsyncGenerator[AsyncIOConnection, None]:
    return pool
