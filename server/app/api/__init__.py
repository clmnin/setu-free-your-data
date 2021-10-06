from fastapi import APIRouter

from . import (
    consent,
    login,
    notification
)

api_router = APIRouter()

api_router.include_router(login.router, tags=["login"])
api_router.include_router(consent.router, prefix="/consent", tags=["consent"])
api_router.include_router(notification.router, prefix="/notification", tags=["notification"])
