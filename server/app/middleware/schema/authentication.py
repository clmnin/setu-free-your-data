from uuid import UUID

from pydantic import BaseModel


class UserToken(BaseModel):
    """
    Decoded token. Used all around the app to validate a user
    """
    id: UUID


class Token(BaseModel):
    """
    Token to be send to the client (as part of BackendUserTokenResponse)
    """
    access_token: str
    token_type: str
