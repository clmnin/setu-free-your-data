from typing import Optional

from fastapi import Header, HTTPException, status, APIRouter
from jose import jwk
import datetime as dt
import uuid

from app.middleware.consent_notification import fetch_signed_consent
from app.middleware.local_file import read_public_key
from app.middleware.schema.Notification import ConsentNotificationRequest, NotificationResponse
from app.middleware.utils.request_signing import validate_detached_jws


router = APIRouter()


@router.post("/Consent/Notification", response_model=NotificationResponse)
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


@router.post("/FI/Notification", response_model=NotificationResponse)
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
