from typing import Optional
from uuid import UUID

from pydantic import BaseModel

from app.middleware.schema.fi_data import FiDeposit, FiTermDeposit, FiRecurringDeposit, FiCreditCard, FiInsurance


class AAAccount(BaseModel):
    id: UUID
    phone: str
    name: Optional[str] = None
    email: Optional[str] = None
    dob: Optional[str] = None
    pan: Optional[str] = None
    fi_deposit: Optional[FiDeposit] = None
    fi_term: Optional[FiTermDeposit] = None
    fi_recurring: Optional[FiRecurringDeposit] = None
    fi_credit_card: Optional[FiCreditCard] = None
    fi_insurance: Optional[FiInsurance] = None
