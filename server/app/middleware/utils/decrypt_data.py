import json

import httpx

from app.middleware.config import settings
import base64


async def decrypt_data(fi, private_key, key_material):
    fi_data = fi[0]
    body = {
        "base64Data": fi_data["data"][0]["encryptedFI"],
        "base64RemoteNonce": fi_data["KeyMaterial"]["Nonce"],
        "base64YourNonce": key_material["Nonce"],
        "ourPrivateKey": private_key,
        "remoteKeyMaterial": fi_data["KeyMaterial"],
    }
    async with httpx.AsyncClient() as client:
        r: httpx.Response = await client.post(settings.RAHASYA_URL + "/ecc/v1/decrypt", json=body)
        res = json.loads(r.text)
        data = base64.b64decode(res['base64Data'])
        return data
