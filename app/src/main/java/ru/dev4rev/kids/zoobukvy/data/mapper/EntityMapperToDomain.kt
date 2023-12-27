package ru.dev4rev.kids.zoobukvy.data.mapper

import ru.dev4rev.kids.zoobukvy.domain.entity.DomainEntity

interface EntityMapperToDomain<T: DomainEntity, E: DataEntity> {
    fun mapToDomain(entity: E): T
}