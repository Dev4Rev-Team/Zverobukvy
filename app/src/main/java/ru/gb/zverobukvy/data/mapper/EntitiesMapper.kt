package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.domain.entity.DomainEntity

interface EntitiesMapper<T : List<DomainEntity>, E : List<DataEntity>> :
    EntitiesMapperToDomain<T, E>,
    EntitiesMapperToData<T, E> {
}