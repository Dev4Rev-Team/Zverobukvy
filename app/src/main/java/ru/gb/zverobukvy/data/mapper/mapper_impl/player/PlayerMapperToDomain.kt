package ru.gb.zverobukvy.data.mapper.mapper_impl.player

import ru.gb.zverobukvy.data.mapper.EntityMapperToDomain
import ru.gb.zverobukvy.data.room.entity.player.PlayerWithAvatar
import ru.gb.zverobukvy.domain.entity.player.Avatar
import ru.gb.zverobukvy.domain.entity.player.Player

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