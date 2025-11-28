from exceptions.base import ResourceNotFoundException


class UserNotFoundException(ResourceNotFoundException):
    template: str = "User not found: uuid={uuid}"