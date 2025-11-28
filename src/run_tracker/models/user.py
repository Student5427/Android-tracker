from sqlalchemy import Integer, String, Numeric
from sqlalchemy.orm import Mapped, mapped_column, relationship
from sqlalchemy.dialects.postgresql import UUID as UUID_DB

from uuid import UUID, uuid4

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from run_tracker.models import UserFriends, UserTraining, UserTrainingPlan


class User(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Пользователь

    Attributes:
        first_name: Имя
        last_name: Фамилия
        patronymic: Отчество
        email: Электронная почта
        weight: Вес  # TODO: ask question
        height: Рост
    """

    __tablename__ = "user"

    id: Mapped[UUID] = mapped_column(UUID_DB(as_uuid=True), primary_key=True, default=uuid4)
    first_name: Mapped[str] = mapped_column(String)
    last_name: Mapped[str] = mapped_column(String)
    patronymic: Mapped[str] = mapped_column(String)
    email: Mapped[str] = mapped_column(String)
    weight: Mapped[float] = mapped_column(Numeric(precision=4, scale=1))
    height: Mapped[int] = mapped_column(Integer)

    friends: Mapped[list["UserFriends"]] = relationship(
        "UserFriends", foreign_keys="[UserFriends.user_id]", back_populates="user"
    )
    friend_of: Mapped[list["UserFriends"]] = relationship(
        "UserFriends", foreign_keys="[UserFriends.friend_id]", back_populates="friend"
    )
    user_trainings: Mapped[list["UserTraining"]] = relationship(
        "UserTraining", back_populates="user", cascade="all, delete-orphan"
    )
    user_training_plans: Mapped[list["UserTrainingPlan"]] = relationship(
        "UserTrainingPlan", back_populates="user", cascade="all, delete-orphan"
    )

    __table_args__ = ({"comment": "Таблица пользователей"},)