package ru.dev4rev.zoobukvy.data.mapper.mapper_impl.player

import ru.dev4rev.zoobukvy.data.mapper.EntityMapperToData
import ru.dev4rev.zoobukvy.data.room.entity.player.PlayerInDatabase
import ru.dev4rev.zoobukvy.domain.entity.player.Player

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