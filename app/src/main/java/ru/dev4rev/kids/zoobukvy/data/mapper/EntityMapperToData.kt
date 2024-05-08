package ru.dev4rev.kids.zoobukvy.data.mapper

import ru.dev4rev.kids.zoobukvy.domain.entity.DomainEntity

interface EntityMapperToData <in T: DomainEntity, out E: DataEntity> {
    fun mapToData(entity: T): E
}