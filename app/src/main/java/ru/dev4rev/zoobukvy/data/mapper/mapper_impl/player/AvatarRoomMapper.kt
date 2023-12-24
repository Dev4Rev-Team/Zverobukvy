package ru.dev4rev.zoobukvy.data.mapper.mapper_impl.player

import ru.dev4rev.zoobukvy.data.mapper.EntityMapper
import ru.dev4rev.zoobukvy.data.room.entity.player.AvatarInDatabase
import ru.dev4rev.zoobukvy.domain.entity.player.Avatar

class AvatarRoomMapper : EntityMapper<Avatar, AvatarInDatabase> {
    override fun mapToData(entity: Avatar): AvatarInDatabase =
        entity.let {
            AvatarInDatabase(
                id = it.id,
                imageName = it.imageName,
                isStandard = it.isStandard
            )
        }

    override fun mapToDomain(entity: AvatarInDatabase): Avatar =
        entity.let {
            Avatar(
                id = it.id,
                imageName = it.imageName,
                isStandard = it.isStandard
            )
        }
}