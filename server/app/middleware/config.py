from pydantic import BaseSettings


class Settings(BaseSettings):
    API_V1_STR: str = "/api/v1"
    ACCESS_TOKEN_EXPIRE_MINUTES: int = 60 * 24 * 8
    SECRET_KEY: str
    API_URL: str
    ANUMATI_URL: str
    CLIENT_API_KEY: str
    RAHASYA_URL: str
    EDGEDB_DB: str


    class Config:
        case_sensitive = True
        env_file = ".env"


settings = Settings()
