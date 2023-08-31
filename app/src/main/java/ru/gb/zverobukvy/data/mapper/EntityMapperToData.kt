package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.domain.entity.DomainEntity

interface EntityMapperToData <T: DomainEntity, E: DataEntity> {
    fun mapToData(entity: T): E
}