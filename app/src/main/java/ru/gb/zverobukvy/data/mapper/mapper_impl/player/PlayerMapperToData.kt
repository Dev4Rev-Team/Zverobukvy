package ru.gb.zverobukvy.data.mapper.mapper_impl.player

import ru.gb.zverobukvy.data.mapper.EntityMapperToData
import ru.gb.zverobukvy.data.room.entity.player.PlayerInDatabase
import ru.gb.zverobukvy.domain.entity.player.Player

class PlayerMapperToData : EntityMapperToData<Player, PlayerInDatabase> {
    override fun mapToData(entity: Player): PlayerInDatabase =
        entity.let {
            PlayerInDatabase(
                idPlayer = it.id,
                name = it.name,
                idAvatar = it.avatar.id,
                rating = it.rating,
                lettersGuessingLevel = it.lettersGuessingLevel
            )
        }
}