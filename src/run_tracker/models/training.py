from datetime import datetime

from sqlalchemy import Integer, String, DateTime, ForeignKey
from sqlalchemy.orm import Mapped, mapped_column

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete


class Training(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Тренировка

    Attributes:
        activity_type: Вид активности (ActivityTypes)  # TODO: create enum ActivityTypes
        training_type_id: Идентификатор типа тренировки
        planned_duration_training: Запланированная длительность тренировки
        actual_duration_training: Действительная длительность тренировки
        status: Статус тренировки (TrainingStatuses)  # TODO: create enum TrainingStatuses
        rpe_scale: Оценка самочувствия по шкале RPE
        training_date: Планируемые дата-время тренировки?  # TODO: ask question
        started_at: Дата-время начала тренировки
        covered_distance: Пройденное расстояние
        preparation_stage_id: Идентификатор этапа подготовки
    """

    __tablename__ = "training"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    activity_type: Mapped[str] = mapped_column(String)
    training_type_id: Mapped[int] = mapped_column(ForeignKey("training_type.id"))
    # planned_duration_training: Mapped[???] = mapped_column(???)  # TODO: choose type
    # actual_duration_training: Mapped[???] = mapped_column(???)  # TODO: choose type
    status: Mapped[str] = mapped_column(String)
    rpe_scale: Mapped[int] = mapped_column(Integer)
    # training_date: Mapped[datetime] = mapped_column(DateTime(timezone=True))
    started_at: Mapped[datetime] = mapped_column(DateTime(timezone=True))
    covered_distance: Mapped[int] = mapped_column(Integer)
    preparation_stage_id: Mapped[int] = mapped_column(ForeignKey("preparation_stage.id"))

    # TODO: relationships

    __table_args__ = ({"comment": "Тренировка"},)