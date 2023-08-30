package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.data.room.entity.LetterCardInDatabase
import ru.gb.zverobukvy.domain.entity.LetterCard

class LetterCardMapperToDomain : EntityMapperToDomain<LetterCard, LetterCardInDatabase> {
    override fun mapToDomain(entity: LetterCardInDatabase): LetterCard =
        entity.let {
            LetterCard(
                letter = it.letter.first(),
                typesCards = ExtractTypesCardsHelper.extractTypesCards(it.typeCardsInDatabase),
                faceImageName = it.faceImageName,
                backImageName = it.backImageName
            )
        }
}