from enum import Enum


class ErrorCodeEnum(Enum):
    """
    Внутренние коды ошибок
    """

    # Является обязательным
    REQUIRED = "required"

    # Запись не найдена
    NOT_FOUND = "not_found"

    # Запись удалена
    RECORD_DELETED = "record_deleted"

    # Произошел конфликт данных
    CONFLICTING_STATE = "conflicting_state"

    # Нарушена уникальность
    ALREADY_EXISTS = "already_exists"

    # Данные являются невалидными
    INVALID_DATA = "invalid_data"