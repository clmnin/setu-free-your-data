from typing import Optional, Dict

from fastapi import FastAPI, Header, HTTPException, status
import httpx
import json
from jose import jwk
import datetime as dt
import uuid

from app.middleware.consent_notification import fetch_signed_consent
from app.middleware.local_file import read_public_key, read_private_key
from app.middleware.schema.Consent import CheckConsentResponse
from app.middleware.schema.Notification import ConsentNotificationRequest, NotificationResponse
from app.middleware.schema.response import ConsentStatus
from app.middleware.utils.consent_detail import create_date
from app.middleware.utils.request_signing import make_detached_jws, validate_detached_jws
from app.middleware.config import settings

app = FastAPI()


@app.get("/consent/{phone_number}", response_model=str)
async def generate_consent_request(phone_number: str) -> str:
    """
    Generate a consent request and respond with url for user to accept consent
    """
    body = create_date(phone_number)
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


@app.post("/consent/fetch/{consent_handle}", response_model=Dict)
async def consent_fetch_signed_consent(consent_handle: str):
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
        if consent_status.status == "READY":
            aa_data = await fetch_signed_consent(str(consent_status.id))
            if aa_data:
                print(json.loads(aa_data))
                return {"status": consent_status.status}
            else:
                return {"status": "Too may requests"}
        else:
            return {"status": consent_status.status}





@app.post("/Consent/Notification", response_model=NotificationResponse)
async def consent_notification(
        body: ConsentNotificationRequest,
        x_jws_signature: Optional[str] = Header(None)
) -> NotificationResponse:
    """
    Notification raised by Setu to let us know of an update on consent request
    """
    if not x_jws_signature:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="x-jws-signature was empty")
    pub_key = await read_public_key()
    if validate_detached_jws(x_jws_signature, body, jwk.construct(pub_key)):
        consent_id = body.ConsentStatusNotification.consentId
        consent_status = body.ConsentStatusNotification.consentStatus
        if consent_status == "ACTIVE":
            await fetch_signed_consent(consent_id)
        date_now = dt.datetime.now()
        return NotificationResponse(
            ver="1.0",
            timestamp=date_now.strftime("%Y-%m-%dT%H:%M:%S.%fZ"),
            txnid=str(uuid.uuid4()),
            response="OK"
        )


@app.post("/FI/Notification", response_model=NotificationResponse)
async def fi_notification(
        body: ConsentNotificationRequest,
        x_jws_signature: Optional[str] = Header(None)
) -> NotificationResponse:
    """
    Notification raised by Setu to let us know of an update on our request to fetch AA data
    """
    pub_key = await read_public_key()
    if validate_detached_jws(x_jws_signature, body, jwk.construct(pub_key)):
        # TODO: Read the body and do something here
        date_now = dt.datetime.now()
        return NotificationResponse(
            ver="1.0",
            timestamp=date_now.strftime("%Y-%m-%dT%H:%M:%S.%fZ"),
            txnid=str(uuid.uuid4()),
            response="OK"
        )
