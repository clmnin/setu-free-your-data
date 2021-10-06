import json
from typing import List, Optional

import httpx

from app.middleware.config import settings
import base64

from app.middleware.schema.Consent import FiTypesEnum


async def decrypt_data(fi, private_key, key_material) -> Optional[List[FiTypesEnum]]:
    try:
        # for each encryptedFI we need to decode the data
        fi_data = fi[0]  # this is always index 0
        count_fi_data = len(fi_data["data"])
        data_list = []
        for i in range(count_fi_data):
            body = {
                "base64Data": fi_data["data"][i]["encryptedFI"],
                "base64RemoteNonce": fi_data["KeyMaterial"]["Nonce"],
                "base64YourNonce": key_material["Nonce"],
                "ourPrivateKey": private_key,
                "remoteKeyMaterial": fi_data["KeyMaterial"],
            }
            async with httpx.AsyncClient() as client:
                r: httpx.Response = await client.post(settings.RAHASYA_URL + "/ecc/v1/decrypt", json=body)
                if r.status_code == 200:
                    res = json.loads(r.text)
                    data = base64.b64decode(res['base64Data'])
                    data_list.append(json.loads(data))
                else:
                    return None
        # parse the data_list to identify FI data received and respond those as a list
        returned_fi_types = [FiTypesEnum[i['account']['type'].upper()] for i in data_list]

        return list(returned_fi_types)
    except Exception as e:
        return None
