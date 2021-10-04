import uuid
import httpx
import json
import datetime as dt
from typing import Dict

from app.middleware.config import settings


async def generate_key_material() -> Dict:
    async with httpx.AsyncClient() as client:
        r = await client.get(settings.RAHASYA_URL + "/ecc/v1/generateKey")
        return json.loads(r.text)


def request_data_body(signed_consent, consent_id, keys):
    date_now = dt.datetime.now()
    data = {
        "ver": "1.0",
        "timestamp": date_now.strftime("%Y-%m-%dT%H:%M:%S.%fZ"),
        "txnid": str(uuid.uuid4()),
        "FIDataRange": {
            "from": "2021-01-06T11:39:57.153Z",
            "to": "2021-06-30T14:25:33.440Z",
        },
        "Consent": {
            "id": consent_id,
            "digitalSignature": signed_consent.split(".")[2],
        },
        "KeyMaterial": keys,
    }
    return data
