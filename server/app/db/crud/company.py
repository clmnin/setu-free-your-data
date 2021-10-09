from typing import Optional, Dict
from uuid import UUID

import json
from edgedb import AsyncIOConnection, NoDataError
from fastapi import HTTPException
from pydantic import parse_raw_as

from app.middleware.schema.company import CompanyWithAA, Ledger, LedgerResponse
from app.middleware.schema.fi_data import BankTransactions


async def create(
    con: AsyncIOConnection, *, name: str, display_name: str
) -> Dict:
    try:
        async for tx in con.retrying_transaction():
            async with tx:
                result = await tx.query_single_json(
                    """
                    SELECT {
                        INSERT Company {
                            name := <str>$name,
                            display_name := <str>$display_name
                        }
                    } {
                        id,
                        name,
                        display_name,
                        aa
                    }
                    """,
                    name=name,
                    display_name=display_name
                )
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"{e}")
    return json.loads(result)


async def get_with_aa(
    con: AsyncIOConnection, *, company_id: UUID
) -> Optional[CompanyWithAA]:
    try:
        async for tx in con.retrying_transaction():
            async with tx:
                result = await tx.query_single_json(
                    """
                    SELECT Company {
                        id,
                        name,
                        display_name,
                        aa : {
                            id,
                            phone,
                            name,
                            email,
                            dob,
                            pan,
                            fi_deposit : {
                                id,
                                maskedAccNumber,
                                type_,
                                branch,
                                status,
                                pending_amt,
                                ifscCode,
                                micrCode
                            },
                            fi_term : {
                                id,
                                maskedAccNumber,
                                ifsc,
                                branch,
                                tenureDays,
                                accountType,
                                currentValue
                            },
                            fi_recurring : {
                                id,
                                maskedAccNumber,
                                ifsc,
                                branch,
                                tenureDays,
                                accountType,
                                currentValue
                            },
                            fi_credit_card : {
                                id,
                                maskedAccNumber,
                                dueDate,
                                cashLimit,
                                currentDue,
                                creditLimit,
                                totalDueAmount,
                                availableCredit,
                                previousDueAmount,
                                lastStatementDate
                            },
                            fi_insurance : {
                                id,
                                maskedAccNumber,
                                coverType,
                                sumAssured,
                                tenureYears,
                                tenureMonths,
                                policyEndDate
                            },
                        }
                    } FILTER .id = <uuid>$company_id
                    """,
                    company_id=company_id
                )
    except NoDataError:
        return None
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"{e}")
    return parse_raw_as(CompanyWithAA, result)


async def get_bank_transactions(
    con: AsyncIOConnection, *, company_id: UUID
) -> Optional[BankTransactions]:
    try:
        async for tx in con.retrying_transaction():
            async with tx:
                result = await tx.query_single_json(
                    """
                    SELECT<json> {
                        data := array_agg((
                            SELECT TransactionLine {
                                id,
                                mode,
                                trans_type,
                                txnId,
                                amount,
                                text,
                                narration,
                                reference,
                                valueDate,
                                currentBalance,
                                transactionTimestamp
                            } FILTER 
                                .<transactions[IS Deposit]
                                .<fi_deposit[IS AAAccount]
                                .<aa[IS Company].id = <uuid>$company_id
                        ))
                    }
                    """,
                    company_id=company_id
                )
    except NoDataError:
        return None
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"{e}")
    return parse_raw_as(BankTransactions, result)


async def get_ledger(
    con: AsyncIOConnection, *, company_id: UUID
) -> Optional[LedgerResponse]:
    try:
        async for tx in con.retrying_transaction():
            async with tx:
                result = await tx.query_single_json(
                    """
                    SELECT<json> {
                        data := array_agg((
                            SELECT LedgerEntry {
                                id,
                                owner_ := .owner.id,
                                party_ := .party.id,
                                lid,
                                type_,
                                amt,
                                bal,
                                narration,
                                write_date
                            } FILTER 
                                .owner.id = <uuid>$company_id
                        ))
                    }
                    """,
                    company_id=company_id
                )
    except NoDataError:
        return None
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"{e}")
    return parse_raw_as(LedgerResponse, result)


async def link_aa(
    con: AsyncIOConnection, *, company_id: UUID, aa_id: UUID
) -> Dict:
    try:
        async for tx in con.retrying_transaction():
            async with tx:
                result = await tx.query_single_json(
                    """
                    SELECT {
                        UPDATE Company FILTER .id = <uuid>$company_id SET {
                            aa := (SELECT AAAccount FILTER .id = <uuid>$aa_id),
                            write_date := (SELECT datetime_current())
                        }
                    } {
                        id,
                        name,
                        display_name
                    }
                    """,
                    company_id=company_id,
                    aa_id=aa_id
                )
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"{e}")
    return json.loads(result)
