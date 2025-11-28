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
        weight: Вес  # TODO: ask question
        height: Рост
    """

    first_name: str = Field(..., description="Имя")
    last_name: str = Field(..., description="Фамилия")
    patronymic: str = Field(..., description="Отчество")
    email: str = Field(..., description="Электронная почта")
    weight: float = Field(..., description="Вес")
    height: float = Field(..., description="Рост")


class UserOutSchema(BaseCamelCaseModel):
    """
    Пользователь

    Attributes:
        id: Идентификатор
        first_name: Имя
        last_name: Фамилия
        patronymic: Отчество
        email: Электронная почта
        weight: Вес  # TODO: ask question
        height: Рост
    """

    id: UUID = Field(..., description="Идентификатор")
    first_name: str = Field(..., description="Имя")
    last_name: str = Field(..., description="Фамилия")
    patronymic: str = Field(..., description="Отчество")
    email: str = Field(..., description="Электронная почта")
    weight: float = Field(..., description="Вес")
    height: float = Field(..., description="Рост")
