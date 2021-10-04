from pydantic import BaseModel
from uuid import UUID


class _Base(BaseModel):
    ver: str
    timestamp: str
    txnid: UUID
