from datetime import datetime
from typing import TYPE_CHECKING

from sqlalchemy import Integer, Numeric, DateTime, ForeignKey
from sqlalchemy.orm import Mapped, mapped_column, relationship

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete

if TYPE_CHECKING:
    from run_tracker.models import TrainingPlan, PreparationStageConfiguration, WeeklyScheme, Training


class PreparationStage(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Этап подготовки

    Attributes:
        date_start: Дата начала этапа
        date_end: Дата завершения этапа
        cycle_volume: Объем цикла в часах (cycle_volume = annual_volume_percent / 100 * planned_annual_volume)
        week_volume: Недельный объем в часах (week_volume = cycle_volume * week_volume_percent / 100)
        planned_speed_training_volume: Запланированный объем времени для скоростных тренировок в минутах
                                       (planned_speed_training_volume = week_volume * 60 * speed_training_percent / 100)
        actual_speed_training_volume: Выполненный объем времени скоростных тренировок в минутах
        planned_tempo_training_volume: Запланированный объем времени для темповых тренировок в минутах
                                       (planned_tempo_training_volume = week_volume * 60 * tempo_training_percent / 100)
        actual_tempo_training_volume: Выполненный объем времени для темповых тренировок в минутах
        planned_interval_training_volume: Запланированный объем времени для интервальных тренировок в минутах
                                 (planned_interval_training_volume = week_volume * 60 * interval_training_percent / 100)
        actual_interval_training_volume: Выполненный объем времени для интервальных тренировок в минутах
        planned_distance_training_volume: Запланированный объем времени для длительных тренировок в минутах
                                 (planned_distance_training_volume = week_volume * 60 * distance_training_percent / 100)
        actual_distance_training_volume: Выполненный объем времени для длительных тренировок в минутах
        planned_strength_training_volume: Запланированный объем времени для силовых тренировок в минутах
                                 (planned_strength_training_volume = week_volume * 60 * strength_training_percent / 100)
        actual_strength_training_volume: Выполненный объем времени для силовых тренировок в минутах
        training_plan_id: Идентификатор плана тренировок
        preparation_stage_configuration_id: Идентификатор конфигурации этапа подготовки
        weekly_scheme_id: Идентификатор недельной схемы тренировок
    """

    __tablename__ = "preparation_stage"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    date_start: Mapped[datetime] = mapped_column(DateTime(timezone=True))
    date_end: Mapped[datetime] = mapped_column(DateTime(timezone=True))
    cycle_volume: Mapped[float] = mapped_column(Numeric(precision=5, scale=2))
    week_volume: Mapped[float] = mapped_column(Numeric(precision=5, scale=2))
    planned_speed_training_volume: Mapped[int] = mapped_column(Integer)
    actual_speed_training_volume: Mapped[int] = mapped_column(Integer)
    planned_tempo_training_volume: Mapped[int] = mapped_column(Integer)
    actual_tempo_training_volume: Mapped[int] = mapped_column(Integer)
    planned_interval_training_volume: Mapped[int] = mapped_column(Integer)
    actual_interval_training_volume: Mapped[int] = mapped_column(Integer)
    planned_distance_training_volume: Mapped[int] = mapped_column(Integer)
    actual_distance_training_volume: Mapped[int] = mapped_column(Integer)
    planned_strength_training_volume: Mapped[int] = mapped_column(Integer)
    actual_strength_training_volume: Mapped[int] = mapped_column(Integer)
    training_plan_id: Mapped[int] = mapped_column(ForeignKey("training_plan.id"))
    preparation_stage_configuration_id: Mapped[int] = mapped_column(ForeignKey("preparation_stage_configuration.id"))
    weekly_scheme_id: Mapped[int] = mapped_column(ForeignKey("weekly_scheme.id"))

    training_plan: Mapped["TrainingPlan"] = relationship(
        "TrainingPlan", back_populates="preparation_stages"
    )
    preparation_stage_configuration: Mapped["PreparationStageConfiguration"] = relationship(
        "PreparationStageConfiguration", back_populates="preparation_stages"
    )
    weekly_scheme: Mapped["WeeklyScheme"] = relationship(
        "WeeklyScheme", back_populates="preparation_stages"
    )

    trainings: Mapped[list["Training"]] = relationship("Training", back_populates="preparation_stage")

    __table_args__ = ({"comment": "Этап подготовки"},)