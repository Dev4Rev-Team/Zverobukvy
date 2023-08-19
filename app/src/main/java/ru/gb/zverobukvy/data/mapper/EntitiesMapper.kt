package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.data.room.entity.DataEntity
import ru.gb.zverobukvy.domain.entity.DomainEntity

interface EntitiesMapper<T: List<DomainEntity>, E: List<DataEntity>> {
    fun mapToDomain(entity: E): T
}