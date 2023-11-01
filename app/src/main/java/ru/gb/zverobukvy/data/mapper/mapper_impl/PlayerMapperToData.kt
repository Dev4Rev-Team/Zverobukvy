package ru.gb.zverobukvy.data.mapper.mapper_impl

import ru.gb.zverobukvy.data.mapper.EntityMapperToData
import ru.gb.zverobukvy.data.room.entity.LettersGuessingLevelInDatabase
import ru.gb.zverobukvy.data.room.entity.PlayerInDatabase
import ru.gb.zverobukvy.domain.entity.LettersGuessingLevel
import ru.gb.zverobukvy.domain.entity.Player

class PlayerMapperToData : EntityMapperToData<Player, PlayerInDatabase> {
    override fun mapToData(entity: Player): PlayerInDatabase =
        entity.let {
            PlayerInDatabase(
                idPlayer = it.id,
                name = it.name,
                idAvatar = it.avatar.id,
                rating = it.rating,
                lettersGuessingLevel = extractLettersGuessingLevel(it.lettersGuessingLevel)
            )
        }

    private fun extractLettersGuessingLevel(lettersGuessingLevel: LettersGuessingLevel): LettersGuessingLevelInDatabase{
        lettersGuessingLevel.run{
            return LettersGuessingLevelInDatabase(
                orangeLevel.first,
                orangeLevel.second,
                greenLevel.first,
                greenLevel.second,
                blueLevel.first,
                blueLevel.second,
                violetLevel.first,
                violetLevel.second
            )
        }
    }
}