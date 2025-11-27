from sqlalchemy import Integer, String
from sqlalchemy.orm import Mapped, mapped_column

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete


class TrainingType(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Тип тренировки

    Attributes:
        name: Название типа тренировки (TrainingTypeName)
        intensity: Интенсивность тренировки (TrainingTypeIntensity)
    """

    __tablename__ = "training_type"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    name: Mapped[str] = mapped_column(String)
    intensity: Mapped[str] = mapped_column(String)

    # TODO: relationships

    __table_args__ = ({"comment": "Тип тренировки"},)