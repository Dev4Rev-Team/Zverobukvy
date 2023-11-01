package ru.gb.zverobukvy.data.mapper.mapper_impl

import ru.gb.zverobukvy.data.mapper.EntityMapperToDomain
import ru.gb.zverobukvy.data.mapper.extract_helpers.ExtractTypesCardsHelper
import ru.gb.zverobukvy.data.room.entity.LetterCardInDatabase
import ru.gb.zverobukvy.domain.entity.LetterCard

class LetterCardMapperToDomain : EntityMapperToDomain<LetterCard, LetterCardInDatabase> {
    override fun mapToDomain(entity: LetterCardInDatabase): LetterCard =
        entity.let {
            LetterCard(
                letter = it.letter.first(),
                typesCards = ExtractTypesCardsHelper.extractTypesCards(it.typeCardsInDatabase),
                faceImageName = it.faceImageName,
                backImageName = it.backImageName,
                soundName = it.soundName
            )
        }
}