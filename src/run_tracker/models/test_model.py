from sqlalchemy import Integer
from sqlalchemy.orm import Mapped, mapped_column

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete


class TestModel(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Тестовая таблица

    Attributes:
        test_int_attr: Тестовый аттрибут
    """

    __tablename__ = "test_model"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    test_int_attr: Mapped[int] = mapped_column(Integer)

    __table_args__ = ({"comment": "Тестовая таблица"},)