package ru.gb.zverobukvy.data.mapper.mapper_impl.player

import ru.gb.zverobukvy.data.mapper.EntityMapperToDomain
import ru.gb.zverobukvy.data.retrofit.AvatarApi
import ru.gb.zverobukvy.domain.entity.player.Avatar

class AvatarApiMapper: EntityMapperToDomain<Avatar, AvatarApi> {
    override fun mapToDomain(entity: AvatarApi): Avatar =
        Avatar(
            imageName = entity.src,
            isStandard = false,
            id = 0
        )
}