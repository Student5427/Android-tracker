from datetime import timedelta
from typing import TYPE_CHECKING

from sqlalchemy import Integer, String, Boolean, ForeignKey, Interval
from sqlalchemy.dialects.postgresql import INT4RANGE, Range
from sqlalchemy.orm import Mapped, mapped_column, relationship

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete

if TYPE_CHECKING:
    from run_tracker.models import Training


class TrainingBlock(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Тренировочный блок

    Attributes:
        name: Название (BlockName)
        distance: Расстояние, которое необходимо преодолеть
        repetition: Количество раз повторения
        duration: Длительность по времени
        order_number: Порядковый номер
        training_zone: Допустимая зона интенсивности
        is_system: Является ли этот блок системным (то есть пользователь не сможет его удалить)
        training_id: Идентификатор тренировки, частью которой является блок
    """

    __tablename__ = "training_block"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    name: Mapped[str] = mapped_column(String)
    distance: Mapped[int] = mapped_column(Integer)
    repetition: Mapped[int] = mapped_column(Integer)
    duration: Mapped[timedelta] = mapped_column(Interval)
    order_number: Mapped[int] = mapped_column(Integer)
    training_zone: Mapped[Range[int]] = mapped_column(INT4RANGE)
    is_system: Mapped[bool] = mapped_column(Boolean, server_default="false")
    training_id: Mapped[int] = mapped_column(ForeignKey("training.id"))

    training: Mapped["Training"] = relationship("Training", back_populates="training_blocks")

    __table_args__ = ({"comment": "Тренировочный блок"},)