from sqlalchemy import Integer, String
from sqlalchemy.orm import Mapped, mapped_column, relationship

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from run_tracker.models import Training


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

    trainings: Mapped[list["Training"]] = relationship("Training", back_populates="training_type")

    __table_args__ = ({"comment": "Тип тренировки"},)