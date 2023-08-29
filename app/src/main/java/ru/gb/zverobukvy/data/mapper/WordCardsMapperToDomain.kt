package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.data.room.entity.WordCardInDatabase
import ru.gb.zverobukvy.domain.entity.WordCard

class WordCardsMapperToDomain: EntitiesMapperToDomain<List<WordCard>, List<WordCardInDatabase>> {
    override fun mapToDomain(entity: List<WordCardInDatabase>): List<WordCard> {
        val wordCards = mutableListOf<WordCard>()
        entity.forEach {
           wordCards.add(
               WordCard(
                    word = it.word,
                    typesCards = ExtractTypesCardsHelper.extractTypesCards(it.typeCardsInDatabase),
                    faceImageName = it.faceImageName,
                )
            )
        }
        return wordCards.toList()
    }
}