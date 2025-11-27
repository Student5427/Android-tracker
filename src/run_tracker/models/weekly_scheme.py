from sqlalchemy import Integer, String
from sqlalchemy.orm import Mapped, mapped_column

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete


class WeeklyScheme(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Недельная схема

    Attributes:
        monday_load: Тип тренировки по интенсивности для понедельника (TrainingLoadType)
        tuesday_load: Тип тренировки по интенсивности для вторника (TrainingLoadType)
        wednesday_load: Тип тренировки по интенсивности для среды (TrainingLoadType)
        thursday_load: Тип тренировки по интенсивности для четверга (TrainingLoadType)
        friday_load: Тип тренировки по интенсивности для пятницы (TrainingLoadType)
        saturday_load: Тип тренировки по интенсивности для субботы (TrainingLoadType)
        sunday_load: Тип тренировки по интенсивности для воскресенья (TrainingLoadType)
    """

    __tablename__ = "weekly_scheme"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    monday_load: Mapped[str] = mapped_column(String)
    tuesday_load: Mapped[str] = mapped_column(String)
    wednesday_load: Mapped[str] = mapped_column(String)
    thursday_load: Mapped[str] = mapped_column(String)
    friday_load: Mapped[str] = mapped_column(String)
    saturday_load: Mapped[str] = mapped_column(String)
    sunday_load: Mapped[str] = mapped_column(String)

    # TODO: relationships

    __table_args__ = ({"comment": "Недельная схема"},)