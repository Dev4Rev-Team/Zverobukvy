package ru.gb.zverobukvy.data.mapper.mapper_impl

import ru.gb.zverobukvy.data.mapper.EntityMapperToDomain
import ru.gb.zverobukvy.data.room.entity.PlayerWithAvatar
import ru.gb.zverobukvy.domain.entity.Avatar
import ru.gb.zverobukvy.domain.entity.Player

class PlayerMapperToDomain : EntityMapperToDomain<Player, PlayerWithAvatar> {
    override fun mapToDomain(entity: PlayerWithAvatar): Player =
        entity.let {
            Player.HumanPlayer(
                name = it.player.name,
                id = it.player.idPlayer,
                avatar = Avatar(
                    id = it.avatar.id,
                    imageName = it.avatar.imageName,
                    isStandard = it.avatar.isStandard
                ),
                rating = it.player.rating,
                lettersGuessingLevel = it.player.lettersGuessingLevel
            )
        }
}