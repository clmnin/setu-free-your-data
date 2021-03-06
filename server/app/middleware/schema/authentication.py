from typing import List, Optional
from uuid import UUID

from pydantic import BaseModel

from app.middleware.schema import users


class TokenPayload(BaseModel):
    """
    UserToken encoded as string using jose.jwt
    """
    sub: str


class UserToken(BaseModel):
    """
    Decoded token. Used all around the app to validate a user
    """
    id: UUID
    companies: Optional[List[UUID]] = None


class Token(BaseModel):
    """
    Token to be send to the client (as part of BackendUserTokenResponse)
    """
    access_token: str
    token_type: str


class BackendUserTokenResponse(BaseModel):
    """
    Token created at backend, to be used by client for all auth requests
    """
    token: Token
    profile: Optional[users.UserProfile]
