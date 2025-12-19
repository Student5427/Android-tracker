from typing import TYPE_CHECKING
from uuid import UUID

from sqlalchemy import ForeignKey
from sqlalchemy.orm import Mapped, mapped_column, relationship

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete

if TYPE_CHECKING:
    from run_tracker.models import User


class UserFriends(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Друзья пользователя, связи пользователей с другими пользователями

    Attributes:
        user_id: Идентификатор пользователя
        friend_id: Идентификатор друга
    """

    __tablename__ = "user_friends"

    user_id: Mapped[UUID] = mapped_column(ForeignKey("user.id"), primary_key=True)
    friend_id: Mapped[UUID] = mapped_column(ForeignKey("user.id"), primary_key=True)

    user: Mapped["User"] = relationship(
        "User", foreign_keys=[user_id], back_populates="friends"
    )
    friend: Mapped["User"] = relationship(
        "User", foreign_keys=[friend_id], back_populates="friend_of"
    )

    __table_args__ = ({"comment": "Таблица друзей пользователя, связи пользователей с другими пользователями"},)