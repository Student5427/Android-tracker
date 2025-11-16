from sqlalchemy import Integer, String
from sqlalchemy.orm import Mapped, mapped_column

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete


class WeeklyScheme(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Недельная схема

    Attributes:
        monday_load: Тип тренировки по интенсивности для понедельника (TrainingLoadTypes)  # TODO: create TrainingLoadTypes enum
        tuesday_load: Тип тренировки по интенсивности для вторника (TrainingLoadTypes)
        wednesday_load: Тип тренировки по интенсивности для среды (TrainingLoadTypes)
        thursday_load: Тип тренировки по интенсивности для четверга (TrainingLoadTypes)
        friday_load: Тип тренировки по интенсивности для пятницы (TrainingLoadTypes)
        saturday_load: Тип тренировки по интенсивности для субботы (TrainingLoadTypes)
        sunday_load: Тип тренировки по интенсивности для воскресенья (TrainingLoadTypes)
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