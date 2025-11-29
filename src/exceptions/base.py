from typing import Any


class BaseExceptionWithMessage(Exception):
    def __init__(self, message: str):
        self.message = message
        super().__init__(message)


class ResourceNotFoundException(BaseExceptionWithMessage):
    template: str = "Not found"

    def __init__(self, *args: Any, **kwargs: Any) -> None:
        self._kw = kwargs
        self.message = self.__str__()
        super().__init__(self.message)

    def __str__(self) -> str:
        return self.template.format(**self._kw)


class ExceptionDetail(BaseExceptionWithMessage):
    """
    Информация об ошибке
    :param field: Поле, для которого произошла ошибка
    :param code: Код ошибки. Используются либо коды pydantic, либо enum ErrorCodeEnum
    :param message: Текст ошибки
    """

    def __init__(self, field: int | str | None, code: str, message: str):
        self.field = field
        self.code = code
        super().__init__(message)


class ValidationException(ExceptionDetail):
    ...


class ConflictingStateException(BaseExceptionWithMessage):
    ...