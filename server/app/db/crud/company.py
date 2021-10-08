from typing import Optional, Dict
from uuid import UUID

import json
from edgedb import AsyncIOConnection, NoDataError
from fastapi import HTTPException


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
