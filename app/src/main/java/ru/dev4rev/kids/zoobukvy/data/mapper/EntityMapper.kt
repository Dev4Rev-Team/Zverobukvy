package ru.dev4rev.kids.zoobukvy.data.mapper

import ru.dev4rev.kids.zoobukvy.domain.entity.DomainEntity

interface EntityMapper<T : DomainEntity, E : DataEntity> :
    EntityMapperToDomain<T, E>,
    EntityMapperToData<T, E>