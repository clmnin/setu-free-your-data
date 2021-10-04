from fastapi import HTTPException, status
from aiofile import async_open
import json


async def read_private_key() -> str:
    try:
        async with async_open("./keys/private_key.pem", 'r') as afp:
            privateKey = await afp.read(length=-1)
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Failed to open secrets. Error {e}"
        )
    return privateKey


async def read_public_key() -> str:
    try:
        async with async_open("./keys/setu_public_key.json", 'r') as afp:
            pub_key_str = await afp.read(length=-1)
            pub_key = json.loads(pub_key_str)
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Failed to open secrets. Error {e}"
        )
    return pub_key
