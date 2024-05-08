package ru.dev4rev.kids.zoobukvy.data.mapper

import ru.dev4rev.kids.zoobukvy.domain.entity.DomainEntity

interface EntityMapperToDomain<out T: DomainEntity, in E: DataEntity> {
    fun mapToDomain(entity: E): T
}