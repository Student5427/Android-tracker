from datetime import datetime
from uuid import UUID

from pydantic import BaseModel, Field

from schemas.base import BaseCamelCaseModel


class UserCreateSchema(BaseModel):
    """
    Данные регистрации нового пользователя

    Attributes:
        first_name: Имя
        last_name: Фамилия
        patronymic: Отчество
        email: Электронная почта
        weight: Вес
        height: Рост
    """

    first_name: str = Field(..., description="Имя")
    last_name: str = Field(..., description="Фамилия")
    patronymic: str = Field(..., description="Отчество")
    email: str = Field(..., description="Электронная почта")
    weight: float = Field(..., description="Вес")
    height: int = Field(..., description="Рост")


class UserOutSchema(BaseCamelCaseModel):
    """
    Пользователь

    Attributes:
        id: Идентификатор
        first_name: Имя
        last_name: Фамилия
        patronymic: Отчество
        email: Электронная почта
        weight: Вес
        height: Рост
    """

    id: UUID = Field(..., description="Идентификатор")
    first_name: str = Field(..., description="Имя")
    last_name: str = Field(..., description="Фамилия")
    patronymic: str = Field(..., description="Отчество")
    email: str = Field(..., description="Электронная почта")
    weight: float = Field(..., description="Вес")
    height: int = Field(..., description="Рост")
    created: datetime = Field(..., description="Дата-время создания")
    created_by: UUID | None = Field(..., description="Идентификатор создавшего")
    updated: datetime = Field(..., description="Дата-время обновления")
    updated_by: UUID | None = Field(..., description="Идентификатор обновившего")
    is_deleted: bool = Field(..., description="Пользователь удален")
    deleted: datetime | None = Field(..., description="Дата-время удаления")
    deleted_by: UUID | None = Field(..., description="Идентификатор удалившего")


class UserUpdateSchema(BaseModel):
    """
    Новые данные пользователя для обновления

    Attributes:
        first_name: Имя
        last_name: Фамилия
        patronymic: Отчество
        email: Электронная почта
        weight: Вес
        height: Рост
    """

    first_name: str = Field(..., description="Имя")
    last_name: str = Field(..., description="Фамилия")
    patronymic: str = Field(..., description="Отчество")
    email: str = Field(..., description="Электронная почта")
    weight: float = Field(..., description="Вес")
    height: int = Field(..., description="Рост")
