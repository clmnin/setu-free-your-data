from datetime import timedelta
from typing import Any

from fastapi import APIRouter, Depends, HTTPException
from fastapi.security import OAuth2PasswordRequestForm
from edgedb import AsyncIOConnection

from app import edb
from app.middleware.config import settings
from app.middleware.schema import authentication
from app.middleware.schema.authentication import UserToken
from app.middleware.security import create_access_token

router = APIRouter()


@router.post("/login", response_model=authentication.Token)
async def login_access_token(
    con: AsyncIOConnection = Depends(edb.get_con),
    form_data: OAuth2PasswordRequestForm = Depends(),
) -> Any:
    """
    Used only in development server. In production this route should have auth header
    """
    access_token_expires = timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES)

    token = authentication.Token(
        access_token=create_access_token(
            UserToken(id="abf82469-dbaa-4229-ac0c-3e6c6a51dfef"),
            expires_delta=access_token_expires,
        ),
        token_type="bearer",
    )
    return token
