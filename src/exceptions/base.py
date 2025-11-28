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