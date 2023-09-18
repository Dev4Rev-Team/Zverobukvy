package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.data.room.entity.PlayerInDatabase
import ru.gb.zverobukvy.domain.entity.Player

class PlayerMapperToData : EntityMapperToData<Player.HumanPlayer, PlayerInDatabase> {
    override fun mapToData(entity: Player.HumanPlayer): PlayerInDatabase =
        entity.let {
            PlayerInDatabase(
                idPlayer = it.id,
                name = it.name,
                idAvatar = it.avatar.id
            )
        }
}