import asyncio
import logging
from typing import Optional

from edgedb import AsyncIOConnection, async_connect

from app.middleware.config import settings

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

max_tries = 60 * 5  # 5 minutes
wait_seconds = 1


async def check_db() -> Optional[AsyncIOConnection]:
    for attempt in range(max_tries):
        try:
            con = await async_connect(
                dsn=settings.EDGEDB_DB
            )
            # Try to create session to check if DB is awake
            await con.execute("SELECT 1")
            return con
        except Exception as e:
            if attempt < max_tries - 1:
                logger.error(
                    f"""{e}Attempt {attempt + 1}/{max_tries} to connect to database, waiting {wait_seconds}s."""
                )
                await asyncio.sleep(wait_seconds)
            else:
                raise e
    return None
