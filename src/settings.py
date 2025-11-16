from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    DATABASE_URL: str

    API_SERVICE_PORT: int

    DB_ENGINE_POOL_PRE_PING: bool = True
    DB_ENGINE_POOL_RECYCLE: int = -1
    DB_ENGINE_POOL_SIZE: int = 5
    DB_ENGINE_MAX_OVERFLOW: int = 10
    DB_ENGINE_POOL_TIMEOUT: int = 30

    @property
    def db_url(self) -> str:
        return self.DATABASE_URL

    model_config = SettingsConfigDict(env_file="../.env", extra="ignore")


settings = Settings()