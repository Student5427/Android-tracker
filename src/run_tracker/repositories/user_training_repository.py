from typing import Callable, Sequence, Iterable, Any
from uuid import UUID

from fastapi_pagination import Page
from sqlalchemy import select
from sqlalchemy.orm import joinedload

from repositories.base import BaseRepository
from run_tracker.models import UserTraining, Training
from run_tracker.schemas.user_training import UserTrainingCreateSchema, UserTrainingUpdateSchema


class UserTrainingRepository(BaseRepository):
    async def create_user_training(self, user_id: UUID, payload: UserTrainingCreateSchema) -> Training:
        training = await self.save(
            Training(
                activity_type=payload.activity_type,
                training_type_id=payload.training_type_id,
                planned_duration_training=payload.planned_duration_training,
                actual_duration_training=None,
                status=payload.status,
                rpe_scale=None,
                date=payload.date,
                started_at=None,
                covered_distance=None,
                preparation_stage_id=payload.preparation_stage_id,
            )
        )
        user_training = await self.save(UserTraining(user_id=user_id, training_id=training.id))

        training.user_training = user_training

        return training

    async def get_user_trainings(
        self, user_id: UUID, transformer: Callable[[Sequence[Training]], Iterable[Any]] = lambda rows: list(rows)
    ) -> Page[Any]:
        statement = (
            select(Training)
            .options(joinedload(Training.user_training), joinedload(Training.training_type))
            .where(UserTraining.user_id == user_id)
        )

        return await self.paginate(statement, transformer=transformer)

    async def get_user_training_by_id(self, training_id: int) -> Training:
        statement = (
            select(Training)
            .options(joinedload(Training.user_training), joinedload(Training.training_type))
            .where(Training.id == training_id)
        )

        return (await self.session.execute(statement)).scalars().unique().one_or_none()

    async def update_user_training(self, training: Training, payload: UserTrainingUpdateSchema) -> Training:
        training.activity_type = payload.activity_type.value
        training.training_type_id = payload.training_type_id
        training.planned_duration_training = payload.planned_duration_training
        training.actual_duration_training = payload.actual_duration_training
        training.status = payload.status.value
        training.rpe_scale = payload.rpe_scale
        training.date = payload.date
        training.started_at = payload.started_at
        training.covered_distance = payload.covered_distance
        training.preparation_stage_id = payload.preparation_stage_id

        return await self.save(training)
