from enum import Enum
from typing import Optional, List

from pydantic import BaseModel, constr
from uuid import UUID

from app.middleware.schema.base import _Base


class FiTypesEnum(str, Enum):
    DEPOSIT = 'DEPOSIT'
    TERM_DEPOSIT = 'TERM_DEPOSIT'
    RECURRING_DEPOSIT = 'RECURRING_DEPOSIT'
    CREDIT_CARD = 'CREDIT_CARD'
    INSURANCE_POLICIES = 'INSURANCE_POLICIES'


class CreateConsent(BaseModel):
    phone: constr(max_length=10, min_length=10)
    fi_types: List[FiTypesEnum]

    class Config:
        use_enum_values = True


class _ConsentStatusObj(BaseModel):
    id: Optional[UUID] = None
    status: str


class CheckConsentResponse(_Base):
    ConsentHandle: UUID
    ConsentStatus: _ConsentStatusObj


class ConsentStatusEnum(str, Enum):
    READY = 'READY'
    PENDING = 'PENDING'
    ERROR = 'ERROR'


class ConsentStatusResponse(BaseModel):
    status: ConsentStatusEnum
    fi_types: Optional[List[FiTypesEnum]]


class ConsentStatus(BaseModel):
    id: Optional[UUID] = None
    status: str
