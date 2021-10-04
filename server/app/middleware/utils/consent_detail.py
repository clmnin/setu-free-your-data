from typing import Dict
import uuid
import datetime as dt


def create_date(mobile_number: str) -> Dict:
    date_now = dt.datetime.now()
    expiry = date_now + dt.timedelta(seconds=600000)
    data = {
        "ver": "1.0",
        "timestamp": date_now.strftime("%Y-%m-%dT%H:%M:%S.%fZ"),
        "txnid": str(uuid.uuid4()),
        "ConsentDetail": {
            "consentStart": date_now.strftime("%Y-%m-%dT%H:%M:%S.%fZ"),
            "consentExpiry": expiry.strftime("%Y-%m-%dT%H:%M:%S.%fZ"),
            "consentMode": "VIEW",
            "fetchType": "ONETIME",
            "consentTypes": ["TRANSACTIONS", "PROFILE", "SUMMARY"],
            "fiTypes": ["DEPOSIT"],
            "DataConsumer": { "id": "FIU" },
            "Customer": {"id": mobile_number + "@setu-aa"},
            "Purpose": {
                "code": "101",
                "refUri": "https://api.rebit.org.in/aa/purpose/101.xml",
                "text": "Wealth management service",
                "Category": {"type": "string"},
            },
            "FIDataRange": {
                "from": "2021-01-06T11:39:57.153Z",
                "to": "2021-06-30T14:25:33.440Z",
            },
            "DataLife": {"unit": "MONTH", "value": 0},
            "Frequency": {"unit": "MONTH", "value": 5},
            "DataFilter": [
                {
                    "type": "TRANSACTIONAMOUNT",
                    "operator": ">=",
                    "value": "10",
                },
            ],
        }
    }
    return data
