from typing import Optional

from pydantic import BaseModel
from uuid import UUID


class ConsentStatus(BaseModel):
    id: Optional[UUID] = None
    status: str
