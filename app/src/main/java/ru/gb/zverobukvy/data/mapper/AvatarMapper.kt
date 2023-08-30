package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.data.room.entity.AvatarInDatabase
import ru.gb.zverobukvy.domain.entity.Avatar

class AvatarMapper : EntityMapper<Avatar, AvatarInDatabase> {
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