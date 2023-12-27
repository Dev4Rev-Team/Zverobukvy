package ru.dev4rev.kids.zoobukvy.data.room.entity.player

import androidx.room.Embedded
import ru.dev4rev.kids.zoobukvy.data.mapper.DataEntity

class PlayerWithAvatar(
    @Embedded
    val player: PlayerInDatabase,
    @Embedded
    val avatar: AvatarInDatabase
): DataEntity
