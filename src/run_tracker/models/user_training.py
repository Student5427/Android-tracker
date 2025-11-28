from sqlalchemy import ForeignKey
from sqlalchemy.orm import Mapped, mapped_column, relationship

from uuid import UUID

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from run_tracker.models import User, Training


class UserTraining(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Тренировка пользователя

    Attributes:
        user_id: Идентификатор пользователя
        training_id: Идентификатор тренировки
    """

    __tablename__ = "user_training"

    user_id: Mapped[UUID] = mapped_column(ForeignKey("user.id"), primary_key=True)
    training_id: Mapped[UUID] = mapped_column(ForeignKey("training.id"), primary_key=True)

    user: Mapped["User"] = relationship("User", back_populates="user_trainings")
    training: Mapped["Training"] = relationship("Training", back_populates="user_training")

    __table_args__ = ({"comment": "Таблица тренировок пользователя, связи пользователей с их тренировками"},)