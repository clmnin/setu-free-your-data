from datetime import timedelta
from typing import Any

from fastapi import APIRouter, Depends, HTTPException
from fastapi.security import OAuth2PasswordRequestForm
from edgedb import AsyncIOConnection

from app import edb, db
from app.middleware.config import settings
from app.middleware.schema import authentication
from app.middleware.schema.authentication import UserToken
from app.middleware.security import create_access_token

router = APIRouter()


@router.post("/login", response_model=authentication.BackendUserTokenResponse)
async def login_access_token(
    con: AsyncIOConnection = Depends(edb.get_con),
    form_data: OAuth2PasswordRequestForm = Depends(),
) -> authentication.BackendUserTokenResponse:
    """
    Used only in development server. In production this route should have auth header
    """
    if form_data.password != "123456":
        raise HTTPException(status_code=400, detail="Invalid Password")
    else:
        if user_profile := await db.crud.users.get_user_profile(con, user_filter=form_data.username):
            access_token_expires = timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES)
            if user_profile['company']:
                companies = [x['id'] for x in user_profile['company']]
            else:
                companies = None
            token = authentication.Token(
                access_token=create_access_token(
                    UserToken(id=user_profile['id'], companies=companies),
                    expires_delta=access_token_expires,
                ),
                token_type="bearer",
            )
            backend_user_token = authentication.BackendUserTokenResponse(
                token=token,
                profile=user_profile
            )
            return backend_user_token
        else:
            raise HTTPException(status_code=400, detail="User not found")
