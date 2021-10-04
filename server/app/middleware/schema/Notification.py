from pydantic import BaseModel
from typing import List

from app.middleware.schema.base import _Base


class _NotifierObj(BaseModel):
    type: str
    id: str


class _ConsentStatusNotificationObj(BaseModel):
    consentId: str
    consentHandle: str
    consentStatus: str


class _AccountsFIStatusResponseObj(BaseModel):
    linkRefNumber: str
    FIStatus: str
    description: str


class _FIStatusResponseObj(BaseModel):
    fipID: str
    Accounts: List[_AccountsFIStatusResponseObj]


class _FIStatusNotificationObj(BaseModel):
    sessionId: str
    sessionStatus: str
    FIStatusResponse: List[_FIStatusResponseObj]


class ConsentNotificationRequest(_Base):
    Notifier: _NotifierObj
    ConsentStatusNotification: _ConsentStatusNotificationObj


class FINotificationRequest(_Base):
    Notifier: _NotifierObj
    FIStatusNotification: _FIStatusNotificationObj


class NotificationResponse(_Base):
    response: str
