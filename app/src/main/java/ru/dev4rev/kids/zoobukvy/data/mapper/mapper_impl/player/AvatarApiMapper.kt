package ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.player

import ru.dev4rev.kids.zoobukvy.data.mapper.EntityMapperToDomain
import ru.dev4rev.kids.zoobukvy.data.retrofit.AvatarApi
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Avatar

class AvatarApiMapper: EntityMapperToDomain<Avatar, AvatarApi> {
    override fun mapToDomain(entity: AvatarApi): Avatar =
        Avatar(
            imageName = entity.src,
            isStandard = false,
            id = 0
        )
}