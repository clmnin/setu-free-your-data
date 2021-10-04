from typing import Optional

from pydantic import BaseModel
from uuid import UUID

from app.middleware.schema.base import _Base


class ConsentStatusObj(BaseModel):
    id: Optional[UUID] = None
    status: str


class CheckConsentResponse(_Base):
    ConsentHandle: UUID
    ConsentStatus: ConsentStatusObj
