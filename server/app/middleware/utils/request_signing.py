from typing import Dict

from jose import jwt
import base64
from pydantic import ValidationError


ALGORITHM = "RS256"


def make_detached_jws(private_key: str, body: Dict):
    encoded_jwt = jwt.encode(body, private_key, algorithm=ALGORITHM)
    splitted_jws = encoded_jwt.split(".")
    splitted_jws[1] = ""
    return ".".join(splitted_jws)


def validate_detached_jws(detached_jws, body, public_key) -> bool:
    splitted_jws = detached_jws.split(".")
    s = body
    splitted_jws[1] = base64.urlsafe_b64decode(s + '=' * (4 - len(s) % 4))
    token = ".".join(splitted_jws)
    try:
        payload = jwt.decode(token, public_key, algorithms=[ALGORITHM])
        res = True
    except (jwt.JWTError, ValidationError):
        res = False
    return res



