package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.data.room.entity.WordCardInDatabase
import ru.gb.zverobukvy.domain.entity.WordCard

class WordCardMapperToDomain: EntityMapperToDomain<WordCard, WordCardInDatabase> {
    override fun mapToDomain(entity: WordCardInDatabase): WordCard =
        entity.let{
            WordCard(
                word = it.word,
                typesCards = ExtractTypesCardsHelper.extractTypesCards(it.typeCardsInDatabase),
                faceImageName = it.faceImageName,
            )
        }
}