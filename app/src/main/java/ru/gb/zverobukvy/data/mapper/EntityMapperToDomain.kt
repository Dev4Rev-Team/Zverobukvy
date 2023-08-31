package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.domain.entity.DomainEntity

interface EntityMapperToDomain<T: DomainEntity, E: DataEntity> {
    fun mapToDomain(entity: E): T
}