package ru.dev4rev.zoobukvy.data.mapper

import ru.dev4rev.zoobukvy.domain.entity.DomainEntity

interface EntityMapperToDomain<T: DomainEntity, E: DataEntity> {
    fun mapToDomain(entity: E): T
}