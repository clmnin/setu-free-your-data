from datetime import datetime
from typing import Optional, List
from uuid import UUID

from pydantic import BaseModel


class FiData(BaseModel):
    id: UUID
    maskedAccNumber: str


class TransactionLine(BaseModel):
    id: UUID
    mode: str
    trans_type: str
    txnId: str
    amount: int
    text: Optional[str] = None
    narration: Optional[str] = None
    reference: Optional[str] = None
    valueDate: datetime
    currentBalance: int
    transactionTimestamp: datetime


class FiDeposit(FiData):
    type_: str
    branch: str
    status: bool
    pending_amt: int
    ifscCode: str
    micrCode: str


class BankTransactions(BaseModel):
    data: List[TransactionLine]


class FiFD(FiData):
    ifsc: str
    branch: str
    tenureDays: int
    accountType: str
    currentValue: int


class FiTermDeposit(FiFD):
    pass


class FiRecurringDeposit(FiFD):
    pass


class FiCreditCard(FiData):
    dueDate: datetime
    cashLimit: int
    currentDue: int
    creditLimit: int
    totalDueAmount: int
    availableCredit: int
    previousDueAmount: int
    lastStatementDate: datetime


class FiInsurance(FiData):
    coverType: str
    sumAssured: int
    tenureYears: int
    tenureMonths: int
    policyEndDate: datetime
