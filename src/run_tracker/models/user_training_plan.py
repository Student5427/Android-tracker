from typing import TYPE_CHECKING
from uuid import UUID

from sqlalchemy import ForeignKey
from sqlalchemy.orm import Mapped, mapped_column, relationship

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete

if TYPE_CHECKING:
    from run_tracker.models import User, TrainingPlan


class UserTrainingPlan(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    План тренировок пользователя

    Attributes:
        user_id: Идентификатор пользователя
        training_plan_id: Идентификатор плана тренировки
    """

    __tablename__ = "user_training_plan"

    user_id: Mapped[UUID] = mapped_column(ForeignKey("user.id"), primary_key=True)
    training_plan_id: Mapped[int] = mapped_column(ForeignKey("training_plan.id"), primary_key=True)

    user: Mapped["User"] = relationship("User", back_populates="user_training_plans")
    training_plan: Mapped["TrainingPlan"] = relationship("TrainingPlan", back_populates="user_training_plan")

    __table_args__ = (
        {"comment": "Таблица планов тренировок пользователя, связи пользователей с их планами тренировками"},
    )