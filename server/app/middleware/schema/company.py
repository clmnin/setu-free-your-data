from uuid import UUID

from pydantic import BaseModel

from app.middleware.schema.aaaccount import AAAccount


class CompanyBase(BaseModel):
    id: UUID
    name: str
    display_name: str


class CompanyWithAA(CompanyBase):
    aa: AAAccount
