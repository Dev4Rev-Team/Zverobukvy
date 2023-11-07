package ru.gb.zverobukvy.data.mapper.mapper_impl.player

import ru.gb.zverobukvy.data.mapper.EntityMapper
import ru.gb.zverobukvy.data.room.entity.player.AvatarInDatabase
import ru.gb.zverobukvy.domain.entity.player.Avatar

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