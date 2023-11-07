package ru.gb.zverobukvy.data.mapper.mapper_impl.card

import ru.gb.zverobukvy.data.mapper.EntityMapperToDomain
import ru.gb.zverobukvy.data.room.entity.card.LetterCardInDatabase
import ru.gb.zverobukvy.domain.entity.card.LetterCard

class LetterCardMapperToDomain : EntityMapperToDomain<LetterCard, LetterCardInDatabase> {
    override fun mapToDomain(entity: LetterCardInDatabase): LetterCard =
        entity.let {
            LetterCard(
                letter = it.letter.first(),
                faceImageName = it.faceImageName,
                backImageName = it.backImageName,
                soundName = it.soundName
            )
        }
}