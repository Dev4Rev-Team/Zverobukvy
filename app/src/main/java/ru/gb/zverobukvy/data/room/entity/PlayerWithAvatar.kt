package ru.gb.zverobukvy.data.room.entity

import androidx.room.Embedded
import ru.gb.zverobukvy.data.mapper.DataEntity

class PlayerWithAvatar(
    @Embedded
    val player: PlayerInDatabase,
    @Embedded
    val avatar: AvatarInDatabase
): DataEntity
