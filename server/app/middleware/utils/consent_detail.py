from typing import Dict
import uuid
import datetime as dt

from app.middleware.schema.Consent import CreateConsent


def create_date(data: CreateConsent) -> Dict:
    date_now = dt.datetime.now()
    expiry = date_now + dt.timedelta(seconds=600000)
    data = {
        "ver": "1.0",
        "timestamp": date_now.strftime("%Y-%m-%dT%H:%M:%S.%fZ"),
        "txnid": str(uuid.uuid4()),
        "ConsentDetail": {
            "consentStart": date_now.strftime("%Y-%m-%dT%H:%M:%S.%fZ"),
            "consentExpiry": expiry.strftime("%Y-%m-%dT%H:%M:%S.%fZ"),
            "consentMode": "STORE",
            "fetchType": "PERIODIC",
            "consentTypes": ["TRANSACTIONS", "PROFILE", "SUMMARY"],
            "fiTypes": data.fi_types,
            "DataConsumer": {"id": "FIU"},
            "Customer": {"id": data.phone + "@setu-aa"},
            "Purpose": {
                "code": "104",  # Explicit consent to monitor the accounts
                "refUri": "https://api.rebit.org.in/aa/purpose/101.xml",
                "text": "Automatic Ledger Management",
                "Category": {"type": "string"},
            },
            "FIDataRange": {
                "from": "2021-01-06T11:39:57.153Z",
                "to": "2021-06-30T14:25:33.440Z",
            },
            "DataLife": {"unit": "MONTH", "value": 1},
            "Frequency": {"unit": "HOUR", "value": 96},
            "DataFilter": [
                {
                    "type": "TRANSACTIONAMOUNT",
                    "operator": ">=",
                    "value": "1",
                },
            ],
        }
    }
    return data
