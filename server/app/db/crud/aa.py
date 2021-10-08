from typing import Optional, Dict
from uuid import UUID

import json
from edgedb import AsyncIOConnection, NoDataError
from fastapi import HTTPException


async def get(
    con: AsyncIOConnection, *, phone: str
) -> Optional[Dict]:
    try:
        async for tx in con.retrying_transaction():
            async with tx:
                result = await tx.query_single_json(
                    """
                    SELECT AAAccount {
                        id,
                        phone,
                        name,
                        email,
                        dob,
                        pan,
                        fi_deposit,
                        fi_term,
                        fi_recurring,
                        fi_credit_card,
                        fi_insurance
                    } FILTER .phone = <str>$aa_phone
                    """,
                    aa_phone=phone
                )
    except NoDataError:
        return None
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"{e}")
    return json.loads(result)
