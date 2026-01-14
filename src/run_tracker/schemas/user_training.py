from datetime import datetime, timedelta
from uuid import UUID

from pydantic import BaseModel, Field

from enums.training import TrainingTypeName, TrainingTypeIntensity, ActivityType, TrainingStatus
from schemas.base import BaseCamelCaseModel


class UserTrainingCreateSchema(BaseModel):
    """
    Данные новой тренировки пользователя

    Attributes:
        activity_type: Вид активности (ActivityType)
        training_type_id: Идентификатор типа тренировки
        planned_duration_training: Запланированная длительность тренировки
        status: Статус тренировки (TrainingStatus)
        date: Планируемые дата-время тренировки
        preparation_stage_id: Идентификатор этапа подготовки
    """

    activity_type: ActivityType = Field(..., description="Вид активности (ActivityType)")
    training_type_id: int = Field(..., description="Идентификатор типа тренировки")
    planned_duration_training: timedelta = Field(..., description="Запланированная длительность тренировки")
    status: TrainingStatus = Field(..., description="Статус тренировки (TrainingStatus)")
    date: datetime = Field(..., description="Планируемые дата-время тренировки")
    preparation_stage_id: int = Field(..., description="Идентификатор этапа подготовки")


class TrainingTypeOutSchema(BaseCamelCaseModel):
    """
    Тип тренировки

    Attributes:
        name: Название типа тренировки (TrainingTypeName)
        intensity: Интенсивность тренировки (TrainingTypeIntensity)
    """

    id: int = Field(..., description="Идентификатор")
    name: TrainingTypeName = Field(..., description="Название типа тренировки (TrainingTypeName)")
    intensity: TrainingTypeIntensity = Field(..., description="Интенсивность тренировки (TrainingTypeIntensity)")


class UserTrainingListOutSchema(BaseCamelCaseModel):
    """
    Элемент списка тренировок пользователя

    Attributes:
        user_id: Идентификатор пользователя
        id: Идентификатор тренировки
        activity_type: Вид активности (ActivityType)
        training_type: Тип тренировки
        status: Статус тренировки (TrainingStatus)
        date: Планируемые дата-время тренировки
        started_at: Фактические дата-время начала тренировки
        is_deleted: Тренировка удалена
    """

    user_id: UUID = Field(..., description="Идентификатор пользователя")
    id: int = Field(..., description="Идентификатор тренировки")
    activity_type: ActivityType = Field(..., description="Вид активности (ActivityType)")
    training_type: TrainingTypeOutSchema = Field(..., description="Тип тренировки")
    status: TrainingStatus = Field(..., description="Статус тренировки (TrainingStatus)")
    date: datetime = Field(..., description="Планируемые дата-время тренировки")
    started_at: datetime | None = Field(..., description="Фактические дата-время начала тренировки")
    is_deleted: bool = Field(..., description="Тренировка удалена")


class UserTrainingOutSchema(BaseCamelCaseModel):
    """
    Тренировка пользователя

    Attributes:
        user_id: Идентификатор пользователя
        id: Идентификатор тренировки
        activity_type: Вид активности (ActivityType)
        training_type: Тип тренировки
        planned_duration_training: Запланированная длительность тренировки
        actual_duration_training: Фактическая длительность тренировки
        status: Статус тренировки (TrainingStatus)
        rpe_scale: Оценка самочувствия по шкале RPE
        date: Планируемые дата-время тренировки
        started_at: Фактические дата-время начала тренировки
        covered_distance: Пройденное расстояние
        # preparation_stage: Этап подготовки  TODO: create preparation_stage
        created: Дата-время создания
        created_by: Идентификатор создавшего
        updated: Дата-время обновления
        updated_by: Идентификатор обновившего
        is_deleted: Тренировка удалена
        deleted: Дата-время удаления
        deleted_by: Идентификатор удалившего
    """

    user_id: UUID = Field(..., description="Идентификатор пользователя")
    id: int = Field(..., description="Идентификатор тренировки")
    activity_type: ActivityType = Field(..., description="Вид активности (ActivityType)")
    training_type: TrainingTypeOutSchema = Field(..., description="Тип тренировки")
    planned_duration_training: timedelta = Field(..., description="Запланированная длительность тренировки")
    actual_duration_training: timedelta | None = Field(..., description="Фактическая длительность тренировки")
    status: TrainingStatus = Field(..., description="Статус тренировки (TrainingStatus)")
    rpe_scale: int | None = Field(..., description="Оценка самочувствия по шкале RPE")
    date: datetime = Field(..., description="Планируемые дата-время тренировки")
    started_at: datetime | None = Field(..., description="Фактические дата-время начала тренировки")
    covered_distance: int | None = Field(..., description="Пройденное расстояние")
    # preparation_stage: ... = Field(..., description="Этап подготовки")  TODO: create preparation_stage
    created: datetime = Field(..., description="Дата-время создания")
    created_by: UUID | None = Field(..., description="Идентификатор создавшего")
    updated: datetime = Field(..., description="Дата-время обновления")
    updated_by: UUID | None = Field(..., description="Идентификатор обновившего")
    is_deleted: bool = Field(..., description="Тренировка удалена")
    deleted: datetime | None = Field(..., description="Дата-время удаления")
    deleted_by: UUID | None = Field(..., description="Идентификатор удалившего")


class UserTrainingUpdateSchema(BaseModel):
    """
    Новые данные тренировки пользователя для обновления

    Attributes:
        activity_type: Вид активности (ActivityType)
        training_type_id: Идентификатор типа тренировки
        planned_duration_training: Запланированная длительность тренировки
        actual_duration_training: Фактическая длительность тренировки
        status: Статус тренировки (TrainingStatus)
        rpe_scale: Оценка самочувствия по шкале RPE
        date: Планируемые дата-время тренировки
        started_at: Фактические дата-время начала тренировки
        covered_distance: Пройденное расстояние
        preparation_stage_id: Идентификатор этапа подготовки
    """

    activity_type: ActivityType = Field(..., description="Вид активности (ActivityType)")
    training_type_id: int = Field(..., description="Идентификатор типа тренировки")
    planned_duration_training: timedelta = Field(..., description="Запланированная длительность тренировки")
    actual_duration_training: timedelta | None = Field(default=None, description="Фактическая длительность тренировки")
    status: TrainingStatus = Field(..., description="Статус тренировки (TrainingStatus)")
    rpe_scale: int | None = Field(default=None, description="Оценка самочувствия по шкале RPE")
    date: datetime = Field(..., description="Планируемые дата-время тренировки")
    started_at: datetime | None = Field(default=None, description="Фактические дата-время начала тренировки")
    covered_distance: int | None = Field(default=None, description="Пройденное расстояние")
    preparation_stage_id: int | None = Field(None, description="Идентификатор этапа подготовки")  # TODO: make not nullable

