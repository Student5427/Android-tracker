from typing import Sequence, Iterable
from uuid import UUID

from fastapi import Depends
from fastapi_pagination import Page
from sqlalchemy.ext.asyncio import AsyncSession

from core.database import get_session
from enums.error_code import ErrorCodeEnum
from enums.training import TrainingTypeName, ActivityType, TrainingStatus, TrainingTypeIntensity
from exceptions.base import ValidationException
from run_tracker.exceptions.user_training import UserTrainingNotFoundException
from run_tracker.models import Training
from run_tracker.repositories.user_training_repository import UserTrainingRepository
from run_tracker.schemas.user_training import (
    UserTrainingCreateSchema,
    TrainingTypeOutSchema,
    UserTrainingListOutSchema,
    UserTrainingOutSchema,
    UserTrainingUpdateSchema,
)
from run_tracker.services.user_service import UserService


class UserTrainingService:
    def __init__(
        self,
        session: AsyncSession = Depends(get_session),
        user_service: UserService = Depends(),
    ) -> None:
        self.repository = UserTrainingRepository(session)
        self.user_service = user_service

    async def create_user_training(self, user_id: UUID, payload: UserTrainingCreateSchema) -> int:
        await self.user_service.get_user_by_id_or_raise_404(user_id)

        # TODO: add validations

        training = await self.repository.create_user_training(user_id, payload)

        return training.id

    async def get_user_trainings(self, user_id: UUID) -> Page[UserTrainingListOutSchema]:
        await self.user_service.get_user_by_id_or_raise_404(user_id)

        def transformer(trainings: Sequence[Training]) -> Iterable[UserTrainingListOutSchema]:
            result = []

            for training in trainings:
                result.append(
                    UserTrainingListOutSchema(
                        user_id=training.user_training.user_id,
                        id=training.id,
                        activity_type=ActivityType(training.activity_type),
                        training_type=TrainingTypeOutSchema(
                            id=training.training_type.id,
                            name=TrainingTypeName(training.training_type.name),
                            intensity=TrainingTypeIntensity(training.training_type.intensity),
                        ),
                        status=TrainingStatus(training.status),
                        date=training.date,
                        started_at=training.started_at,
                        is_deleted=training.is_deleted,
                    )
                )

            return result

        return await self.repository.get_user_trainings(user_id, transformer)

    async def get_user_training_by_id_or_raise_404(self, training_id: int) -> Training:
        training = await self.repository.get_user_training_by_id(training_id)
        if not training:
            raise UserTrainingNotFoundException(id=training_id)

        return training

    @staticmethod
    def get_user_training_out_schema(training: Training) -> UserTrainingOutSchema:
        return UserTrainingOutSchema(
            user_id=training.user_training.user_id,
            id=training.id,
            activity_type=ActivityType(training.activity_type),
            training_type=TrainingTypeOutSchema(
                id=training.training_type.id,
                name=TrainingTypeName(training.training_type.name),
                intensity=TrainingTypeIntensity(training.training_type.intensity),
            ),
            planned_duration_training=training.planned_duration_training,
            actual_duration_training=training.actual_duration_training,
            status=TrainingStatus(training.status),
            rpe_scale=training.rpe_scale,
            date=training.date,
            started_at=training.started_at,
            covered_distance=training.covered_distance,
            # preparation_stage=training.preparation_stage,  TODO: create preparation_stage
            created=training.created,
            created_by=training.created_by,
            updated=training.updated,
            updated_by=training.updated_by,
            is_deleted=training.is_deleted,
            deleted=training.deleted,
            deleted_by=training.deleted_by,
        )

    async def get_user_training_by_id(self, user_id: UUID, training_id: int) -> UserTrainingOutSchema:
        await self.user_service.get_user_by_id_or_raise_404(user_id)

        training = await self.get_user_training_by_id_or_raise_404(training_id)

        return self.get_user_training_out_schema(training)

    async def update_user_training(
        self, user_id: UUID, training_id: int, payload: UserTrainingUpdateSchema
    ) -> UserTrainingOutSchema:
        await self.user_service.get_user_by_id_or_raise_404(user_id)

        training = await self.get_user_training_by_id_or_raise_404(training_id)

        if training.is_deleted:
            raise ValidationException(
                field="is_deleted",
                code=ErrorCodeEnum.RECORD_DELETED.value,
                message="Cannot update a deleted record, recover it first",
            )

        # TODO: add validations

        training = await self.repository.update_user_training(training, payload)
        return self.get_user_training_out_schema(training)