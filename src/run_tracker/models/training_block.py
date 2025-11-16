from sqlalchemy import Integer, String, Boolean, ForeignKey
from sqlalchemy.orm import Mapped, mapped_column

from models.base import BaseWithCreateAndUpdateTime, BaseWithDelete


class TrainingBlock(BaseWithCreateAndUpdateTime, BaseWithDelete):
    """
    Тренировочный блок

    Attributes:
        name: Название (BlockNames)  # TODO: create BlockNames enum
        distance: Расстояние, которое необходимо преодолеть
        repetition: Количество раз повторения
        duration: Длительность по времени
        order_number: Порядковый номер
        training_zone: Допустимая зона интенсивности
        is_system: Является ли этот блок системным (то есть пользователь никак не сможет его удалить)
        training_id: Идентификатор тренировки, частью которой является блок
    """

    __tablename__ = "training_block"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    name: Mapped[str] = mapped_column(String)
    distance: Mapped[int] = mapped_column(Integer)
    repetition: Mapped[int] = mapped_column(Integer)
    # duration: Mapped[???] = mapped_column(???)  # TODO: choose type
    order_number: Mapped[int] = mapped_column(Integer)
    # training_zone: Mapped[???] = mapped_column(???)  # TODO: choose type
    is_system: Mapped[bool] = mapped_column(Boolean, server_default="false")
    training_id: Mapped[int] = mapped_column(ForeignKey("training.id"))

    # TODO: relationships

    __table_args__ = ({"comment": "Тренировочный блок"},)