from uuid import UUID

from fastapi import APIRouter, Depends, HTTPException
from edgedb import AsyncIOConnection

from app import edb, db
from app.middleware.schema.company import CompanyWithAA
from app.middleware.schema.fi_data import BankTransactions

router = APIRouter()


@router.get("", response_model=CompanyWithAA)
async def company_aa(
    company_id: UUID,
    con: AsyncIOConnection = Depends(edb.get_con),
) -> CompanyWithAA:
    if company_w_aa := await db.crud.company.get_with_aa(con, company_id=company_id):
        return company_w_aa
    else:
        raise HTTPException(status_code=400, detail="Company not found")


@router.get("/bank-transaction", response_model=BankTransactions)
async def company_bank_transactions(
    company_id: UUID,
    con: AsyncIOConnection = Depends(edb.get_con),
) -> BankTransactions:
    if company_transactions := await db.crud.company.get_bank_transactions(con, company_id=company_id):
        return company_transactions
    else:
        raise HTTPException(status_code=400, detail="Company not found")
