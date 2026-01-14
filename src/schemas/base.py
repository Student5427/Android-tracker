from pydantic import BaseModel, ConfigDict
from pydantic.alias_generators import to_camel


class BaseCamelCaseModel(BaseModel):
    model_config = ConfigDict(alias_generator=to_camel, populate_by_name=True)


class BaseOrmModel(BaseModel):
    model_config = ConfigDict(from_attributes=True)