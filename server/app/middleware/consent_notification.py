from typing import Optional, List

import httpx
import json

from app.middleware.local_file import read_private_key
from app.middleware.schema.Consent import FiTypesEnum
from app.middleware.utils.decrypt_data import decrypt_data
from app.middleware.utils.request_data import generate_key_material, request_data_body
from app.middleware.utils.request_signing import make_detached_jws
from app.middleware.config import settings


async def fetch_signed_consent(consent_id: str) -> Optional[List[FiTypesEnum]]:
    try:
        privateKey = await read_private_key()
        detatched_jws = make_detached_jws(privateKey, {"url": "/Consent/" + consent_id})
        async with httpx.AsyncClient() as client:
            headers = {
                "Content-Type": "application/json",
                "client_api_key": settings.CLIENT_API_KEY,
                "x-jws-signature": detatched_jws,
            }
            r: httpx.Response = await client.get(settings.API_URL + "/Consent/" + consent_id, headers=headers)
            if r.status_code == 200:
                return await fi_data_request(json.loads(r.text)['signedConsent'], consent_id)
            else:
                return None
    except Exception:
        return None


async def fi_data_request(signed_consent: str, consent_id: str) -> Optional[List[FiTypesEnum]]:
    try:
        keys = await generate_key_material()
        request_body = request_data_body(signed_consent, consent_id, keys["KeyMaterial"])
        privateKey = await read_private_key()
        detatched_jws = make_detached_jws(privateKey, request_body)
        async with httpx.AsyncClient() as client:
            headers = {
                "Content-Type": "application/json",
                "client_api_key": settings.CLIENT_API_KEY,
                "x-jws-signature": detatched_jws,
            }
            r: httpx.Response = await client.post(settings.API_URL + "/FI/request", json=request_body, headers=headers)
            if r.status_code == 200:
                res = json.loads(r.text)
                return await fi_data_fetch(res['sessionId'], keys["privateKey"], keys["KeyMaterial"])
            else:
                return None
    except Exception:
        return None


async def fi_data_fetch(session_id, encryption_private_key, key_material) -> Optional[List[FiTypesEnum]]:
    try:
        privateKey = await read_private_key()
        detatched_jws = make_detached_jws(privateKey, {"url": "/FI/fetch/" + session_id})
        async with httpx.AsyncClient() as client:
            headers = {
                "Content-Type": "application/json",
                "client_api_key": settings.CLIENT_API_KEY,
                "x-jws-signature": detatched_jws,
            }
            r: httpx.Response = await client.get(settings.API_URL + "/FI/fetch/" + session_id, headers=headers)
            if r.status_code == 200:
                res = json.loads(r.text)
                return await decrypt_data(res['FI'], encryption_private_key, key_material)
            else:
                return None
    except Exception:
        return None
