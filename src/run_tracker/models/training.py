from datetime import datetime, timedelta

from sqlalchemy import Integer, String, DateTime, ForeignKey, Interval
from sqlalchemy.orm import Mapped, mapped_column, relationship

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from run_tracker.models import PreparationStage, TrainingType, TrainingBlock, UserTraining


class Training(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Тренировка

    Attributes:
        activity_type: Вид активности (ActivityType)
        training_type_id: Идентификатор типа тренировки
        planned_duration_training: Запланированная длительность тренировки
        actual_duration_training: Фактическая длительность тренировки
        status: Статус тренировки (TrainingStatus)
        rpe_scale: Оценка самочувствия по шкале RPE
        date: Планируемые дата-время тренировки?  # TODO: ask question
        started_at: Дата-время начала тренировки
        covered_distance: Пройденное расстояние
        preparation_stage_id: Идентификатор этапа подготовки
    """

    __tablename__ = "training"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    activity_type: Mapped[str] = mapped_column(String)
    training_type_id: Mapped[int] = mapped_column(ForeignKey("training_type.id"))
    planned_duration_training: Mapped[timedelta] = mapped_column(Interval)
    actual_duration_training: Mapped[timedelta] = mapped_column(Interval)
    status: Mapped[str] = mapped_column(String)
    rpe_scale: Mapped[int] = mapped_column(Integer)
    date: Mapped[datetime] = mapped_column(DateTime(timezone=True))
    started_at: Mapped[datetime] = mapped_column(DateTime(timezone=True))
    covered_distance: Mapped[int] = mapped_column(Integer)
    preparation_stage_id: Mapped[int] = mapped_column(ForeignKey("preparation_stage.id"))

    preparation_stage: Mapped["PreparationStage"] = relationship(
        "PreparationStage", back_populates="trainings"
    )

    training_type: Mapped["TrainingType"] = relationship("TrainingType", back_populates="trainings")
    training_blocks: Mapped[list["TrainingBlock"]] = relationship(
        "TrainingBlock",
        back_populates="training",
        cascade="all, delete-orphan",
        order_by="TrainingBlock.order_number",
    )
    user_training: Mapped[list["UserTraining"]] = relationship(
        "UserTraining", back_populates="training"
    )

    __table_args__ = ({"comment": "Тренировка"},)