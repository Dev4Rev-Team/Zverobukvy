package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.domain.entity.DomainEntity

interface EntityMapper<T : DomainEntity, E : DataEntity> :
    EntityMapperToDomain<T, E>,
    EntityMapperToData<T, E> {
}