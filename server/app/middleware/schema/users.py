from typing import List

from pydantic import BaseModel
from uuid import UUID

from app.middleware.schema.company import CompanyBase


class UserProfile(BaseModel):
    id: UUID
    phone: str
    company: List[CompanyBase]
