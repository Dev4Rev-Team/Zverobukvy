package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.data.room.entity.LetterCardInDatabase
import ru.gb.zverobukvy.domain.entity.LetterCard

class LetterCardsMapperToDomain: EntitiesMapperToDomain<List<LetterCard>, List<LetterCardInDatabase>> {
    override fun mapToDomain(entity: List<LetterCardInDatabase>): List<LetterCard> {
        val letterCards = mutableListOf<LetterCard>()
        entity.forEach {
            letterCards.add(
                LetterCard(
                    letter = it.letter.first(),
                    typesCards = ExtractTypesCardsHelper.extractTypesCards(it.colorCards),
                    faceImageName = it.faceImageName,
                    backImageName = it.backImageName
                )
            )
        }
        return letterCards.toList()
    }
}