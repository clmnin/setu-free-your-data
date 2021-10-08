from typing import List

from pydantic import BaseModel
from uuid import UUID


class NestedCompanyForUserProfile(BaseModel):
    id: UUID
    name: str
    display_name: str


class UserProfile(BaseModel):
    id: UUID
    phone: str
    company: List[NestedCompanyForUserProfile]
