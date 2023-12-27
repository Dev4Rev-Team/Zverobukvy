package ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.player

import ru.dev4rev.kids.zoobukvy.data.mapper.EntityMapperToData
import ru.dev4rev.kids.zoobukvy.data.room.entity.player.PlayerInDatabase
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player

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