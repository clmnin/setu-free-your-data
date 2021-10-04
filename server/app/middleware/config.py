from pydantic import BaseSettings


class Settings(BaseSettings):
    API_V1_STR: str = "/api/v1"
    API_URL: str
    ANUMATI_URL: str
    CLIENT_API_KEY: str
    RAHASYA_URL: str


    class Config:
        case_sensitive = True
        env_file = ".env"


settings = Settings()
