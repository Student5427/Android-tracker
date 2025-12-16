from datetime import datetime

from sqlalchemy import Integer, String, DateTime
from sqlalchemy.orm import Mapped, mapped_column

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete


class TrainingPlan(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    План тренировок

    Attributes:
        name: Название
        date_start: Дата начала действия плана
        date_end: Дата завершения плана
        training_hours_per_week: Количество часов для тренировок в неделю
        planned_annual_volume: Планируемый годовой объем (annual_volume = training_hours_per_week * 50)
        actual_annual_volume: Фактический годовой объем
    """

    __tablename__ = "training_plan"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    name: Mapped[str] = mapped_column(String)
    date_start: Mapped[datetime] = mapped_column(DateTime(timezone=True))
    date_end: Mapped[datetime] = mapped_column(DateTime(timezone=True))
    training_hours_per_week: Mapped[int] = mapped_column(Integer)
    planned_annual_volume: Mapped[int] = mapped_column(Integer)
    actual_annual_volume: Mapped[int] = mapped_column(Integer)

    # TODO: relationships

    __table_args__ = ({"comment": "План тренировок"},)