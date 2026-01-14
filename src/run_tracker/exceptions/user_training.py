from exceptions.base import ResourceNotFoundException


class UserTrainingNotFoundException(ResourceNotFoundException):
    template: str = "User training not found: id={id}"