package ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.player

import ru.dev4rev.kids.zoobukvy.data.mapper.EntityMapperToDomain
import ru.dev4rev.kids.zoobukvy.data.room.entity.player.PlayerWithAvatar
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Avatar
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player

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