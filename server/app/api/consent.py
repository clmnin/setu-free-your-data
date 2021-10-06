from fastapi import APIRouter
import httpx
import json

from app.middleware.consent_notification import fetch_signed_consent
from app.middleware.local_file import read_private_key
from app.middleware.schema.Consent import (
    CheckConsentResponse, CreateConsent, ConsentStatusResponse, ConsentStatus, ConsentStatusEnum
)
from app.middleware.utils.consent_detail import create_date
from app.middleware.utils.request_signing import make_detached_jws
from app.middleware.config import settings


router = APIRouter()


@router.post("", response_model=str)
async def generate_consent_request(data: CreateConsent) -> str:
    """
    Generate a consent request and respond with url for user to accept consent

    Pass the phone number and the FI Data types accepted by the user
    """
    body = create_date(data)
    privateKey = await read_private_key()
    detatched_jws = make_detached_jws(privateKey, body)
    async with httpx.AsyncClient() as client:
        headers = {
            "Content-Type": "application/json",
            "client_api_key": settings.CLIENT_API_KEY,
            "x-jws-signature": detatched_jws,
        }
        r: httpx.Response = await client.post(settings.API_URL + "/Consent", json=body, headers=headers)
        url = settings.ANUMATI_URL + "/" + json.loads(r.text)['ConsentHandle'] + "?redirect_url=/redirect"
        return url


@router.get("/{consent_handle}", response_model=ConsentStatusResponse)
async def consent_fetch_signed_consent(consent_handle: str) -> ConsentStatusResponse:
    """
    This is for testing.
    Pass the consent_handle and load the data into edgedb
    """
    privateKey = await read_private_key()
    detatched_jws = make_detached_jws(privateKey, {"url": "/Consent/handle/" + consent_handle})
    async with httpx.AsyncClient() as client:
        headers = {
            "Content-Type": "application/json",
            "client_api_key": settings.CLIENT_API_KEY,
            "x-jws-signature": detatched_jws,
        }
        r: httpx.Response = await client.get(settings.API_URL + "/Consent/handle/" + consent_handle, headers=headers)
        res = json.loads(r.text)
        # This can be `PENDING` or `READY`
        c = CheckConsentResponse.parse_obj(res)
        consent_status = ConsentStatus(id=c.ConsentStatus.id, status=c.ConsentStatus.status)
        if consent_status.status == ConsentStatusEnum.READY:
            aa_fetch_status = await fetch_signed_consent(str(consent_status.id))
            if not aa_fetch_status:
                return ConsentStatusResponse(status=ConsentStatusEnum.ERROR)
            else:
                return ConsentStatusResponse(status=ConsentStatusEnum.READY, fi_types=aa_fetch_status)
        else:
            return ConsentStatusResponse(status=consent_status.status)
