package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.domain.entity.DomainEntity

interface EntitiesMapperToDomain<T: List<DomainEntity>, E: List<DataEntity>> {
    fun mapToDomain(entity: E): T
}