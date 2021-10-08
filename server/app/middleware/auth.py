from typing import Optional

from fastapi import Depends, HTTPException
from fastapi.security import OAuth2PasswordBearer
from jose import jwt
from pydantic import ValidationError, parse_obj_as
from edgedb import AsyncIOConnection

from app.middleware.schema import authentication
from app.middleware import security
from app.middleware.config import settings
from app import db, edb
from app.middleware.schema.users import UserProfile

reusable_oauth2 = OAuth2PasswordBearer(
    tokenUrl=f"{settings.API_V1_STR}/login"
)


async def _get_current_user(
        con: AsyncIOConnection = Depends(edb.get_con),
        token: str = Depends(reusable_oauth2)
) -> UserProfile:
    """
    Parse the token and return current user profile
    Raise exception if
    1. Token decode fails
    2. User not found on redis
    3. Decoded token user.companies != cached user.company
        This happens when a user is added to or removed from a company
        For now we will logout the user. But in the future the client should
        have logic to add remove company data when this happens. This would mean
        no logout at client side and so better user experience.
        TODO: Don't 401 when user.companies mismatch
    """
    try:
        payload = jwt.decode(
            token, settings.SECRET_KEY, algorithms=[security.ALGORITHM]
        )
        token_data = authentication.TokenPayload(**payload)
        user_token_data: authentication.UserToken = authentication.UserToken.parse_raw(token_data.sub)
        user_profile = await db.crud.users.get_user_profile(con, user_filter=user_token_data.id)
        res = parse_obj_as(UserProfile, user_profile)
    except (jwt.JWTError, ValidationError):
        raise HTTPException(status_code=401, detail="Could not validate credentials")
    except:
        raise HTTPException(status_code=401, detail="Please login again")
    return res


async def get_current_active_user(
    current_user: UserProfile = Depends(_get_current_user),
) -> UserProfile:
    return current_user
