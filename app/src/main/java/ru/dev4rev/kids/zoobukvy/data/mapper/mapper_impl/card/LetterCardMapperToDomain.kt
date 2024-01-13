package ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.card

import ru.dev4rev.kids.zoobukvy.data.mapper.EntityMapperToDomain
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.LetterCardInDatabase
import ru.dev4rev.kids.zoobukvy.domain.entity.card.LetterCard

class LetterCardMapperToDomain : EntityMapperToDomain<LetterCard, LetterCardInDatabase> {
    override fun mapToDomain(entity: LetterCardInDatabase): LetterCard =
        entity.let {
            LetterCard(
                letter = it.letter.first(),
                faceImageName = it.faceImageName,
                backImageName = it.backImageName,
                soundName = it.baseSoundName,
                letterName = it.letterName,
                softSoundName = it.softSoundName,
                baseSoundName = it.baseSoundName
            )
        }
}