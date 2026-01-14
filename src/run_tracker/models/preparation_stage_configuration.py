from typing import TYPE_CHECKING

from sqlalchemy import Integer, String, Numeric
from sqlalchemy.orm import Mapped, mapped_column, relationship

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete

if TYPE_CHECKING:
    from run_tracker.models import PreparationStage


class PreparationStageConfiguration(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Конфигурация этапа подготовки

    Attributes:
        stage_name: Название этапа подготовки (StageName)
        week_number: Номер тренировочной недели
        annual_volume_percent: Процент от годового объема
        week_volume_percent: Процент от объема цикла
        speed_training_percent: Процент скоростных тренировок на неделе
        tempo_training_percent: Процент темповых тренировок на неделе
        interval_training_percent: Процент интервальных тренировок на неделе
        distance_training_percent: Процент длительных тренировок на неделе
        strength_training_percent: Процент силовых тренировок на неделе
    """

    __tablename__ = "preparation_stage_configuration"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    stage_name: Mapped[str] = mapped_column(String)
    week_number: Mapped[int] = mapped_column(Integer)
    annual_volume_percent: Mapped[float] = mapped_column(Numeric(precision=5, scale=2))
    week_volume_percent: Mapped[float] = mapped_column(Numeric(precision=5, scale=2))
    speed_training_percent: Mapped[float] = mapped_column(Numeric(precision=5, scale=2))
    tempo_training_percent: Mapped[float] = mapped_column(Numeric(precision=5, scale=2))
    interval_training_percent: Mapped[float] = mapped_column(Numeric(precision=5, scale=2))
    distance_training_percent: Mapped[float] = mapped_column(Numeric(precision=5, scale=2))
    strength_training_percent: Mapped[float] = mapped_column(Numeric(precision=5, scale=2))

    preparation_stages: Mapped[list["PreparationStage"]] = relationship(
        "PreparationStage", back_populates="preparation_stage_configuration"
    )

    __table_args__ = ({"comment": "Конфигурация этапа подготовки"},)