from sqlalchemy import Integer, String
from sqlalchemy.orm import Mapped, mapped_column

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete


class TrainingType(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Тип тренировки

    Attributes:
        training_type_name: Название типа тренировок (TrainingTypeNames)  # TODO: create enum TrainingTypeNames
        training_intensity: Классификация типа тренировки по интенсивности (TrainingIntensities)  # TODO: create enum TrainingIntensities
    """

    __tablename__ = "training_type"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    training_type_name: Mapped[str] = mapped_column(String)
    training_intensity: Mapped[str] = mapped_column(String)

    # TODO: relationships

    __table_args__ = ({"comment": "Тип тренировки"},)