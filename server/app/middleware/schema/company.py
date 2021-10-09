from datetime import datetime
from typing import List
from uuid import UUID

from pydantic import BaseModel

from app.middleware.schema.aaaccount import AAAccount


class CompanyBase(BaseModel):
    id: UUID
    name: str
    display_name: str


class CompanyWithAA(CompanyBase):
    aa: AAAccount


class Ledger(BaseModel):
    id: UUID
    owner_: UUID
    party_: UUID
    lid: int
    type_: str
    amt: int
    bal: int
    narration: str
    write_date: datetime


class LedgerResponse(BaseModel):
    data: List[Ledger]
