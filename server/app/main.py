from fastapi import FastAPI

from app.api import api_router
from app.edb import edb_create_pool, edb_close_pool
from app.middleware.config import settings

app = FastAPI(
    on_startup=[edb_create_pool],
    on_shutdown=[edb_close_pool],
)

app.include_router(api_router, prefix=settings.API_V1_STR)
