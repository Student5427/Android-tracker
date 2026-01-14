from enum import Enum


class StageName(Enum):
    """
    Название этапа подготовки
    """

    BASIC = "BASIC"  # Базовый
    INTERVAL = "INTERVAL"  # Интервальный
    PREPARATORY = "PREPARATORY"  # Подготовительный
    COMPETITIVE = "COMPETITIVE"  # Соревновательный
    RESTORATIVE = "RESTORATIVE"  # Восстановительный


class ActivityType(Enum):
    """
    Вид активности
    """

    RUNNING = "RUNNING"  # Бег
    CYCLING = "CYCLING"  # Велосипед
    WALKING = "WALKING"  # Ходьба


class TrainingStatus(Enum):
    """
    Статус тренировки
    """

    SCHEDULED = "SCHEDULED"  # Запланирована
    COMPLETED = "COMPLETED"  # Выполнена
    NOT_COMPLETED = "NOT_COMPLETED"  # Не выполнена


class BlockName(Enum):
    """
    Название блока тренировки
    """

    NAME1 = "NAME1"  # NAME1
    # TODO: ask question


class TrainingTypeName(Enum):
    """
    Название типа тренировки
    """

    INTERVAL = "INTERVAL"  # Интервальная
    TEMPO = "TEMPO"  # Темповая
    STRENGTH = "STRENGTH"  # Силовая
    DISTANCE = "DISTANCE"  # Дистанционная
    SPEED = "SPEED"  # Скоростная


class TrainingTypeIntensity(Enum):
    """
    Интенсивность типа тренировки
    """

    LOW_INTENSITY = "LOW_INTENSITY"  # Низкоинтенсивная
    HIGH_INTENSITY = "HIGH_INTENSITY"  # Высокоинтенсивная


class TrainingLoadType(Enum):
    """
    Тип тренировки по интенсивности для дня недели
    """

    LOW_INTENSITY = "LOW_INTENSITY"  # Низкоинтенсивная
    HIGH_INTENSITY = "HIGH_INTENSITY"  # Высокоинтенсивная
    REST = "REST"  # Отдых
