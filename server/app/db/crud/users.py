from typing import Optional, Dict, Union
from uuid import UUID

import json
from edgedb import AsyncIOConnection, NoDataError
from fastapi import HTTPException


async def get_user_profile(
        con: AsyncIOConnection, *, user_filter: Union[str, UUID]
) -> Optional[Dict]:
    if isinstance(user_filter, str):
        query_filter = "FILTER .phone = <str>$user_filter"
    elif isinstance(user_filter, UUID):
        query_filter = "FILTER .id = <uuid>$user_filter"
    else:
        raise NotImplementedError("Only phone and id are support when getting user profile")
    try:
        async for tx in con.retrying_transaction():
            async with tx:
                result = await tx.query_single_json(
                    f"""
                    SELECT User {{
                        id,
                        phone,
                        company : {{
                            id,
                            name,
                            display_name
                        }}
                    }} {query_filter} 
                    """,
                    user_filter=user_filter
                )
    except NoDataError:
        return None
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"{e}")
    return json.loads(result)
