package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.domain.entity.DomainEntity

interface EntitiesMapperToData <T: List<DomainEntity>, E: List<DataEntity>> {
    fun mapToData(entity: T): E
}