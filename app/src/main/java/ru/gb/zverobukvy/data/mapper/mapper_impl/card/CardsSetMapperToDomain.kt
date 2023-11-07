package ru.gb.zverobukvy.data.mapper.mapper_impl.card

import ru.gb.zverobukvy.data.mapper.EntityMapperToDomain
import ru.gb.zverobukvy.data.mapper.extract_helpers.ExtractLettersHelper
import ru.gb.zverobukvy.data.mapper.extract_helpers.ExtractTypesCardsHelper
import ru.gb.zverobukvy.data.mapper.extract_helpers.ExtractWordsHelper
import ru.gb.zverobukvy.data.room.entity.card.CardsSetInDatabase
import ru.gb.zverobukvy.domain.entity.card.CardsSet

class CardsSetMapperToDomain: EntityMapperToDomain<CardsSet, CardsSetInDatabase> {
    override fun mapToDomain(entity: CardsSetInDatabase): CardsSet =
        CardsSet(
            ExtractTypesCardsHelper.extractTypeCards(entity.color),
            ExtractLettersHelper.extractLetters(entity.letters),
            ExtractWordsHelper.extractWords(entity.words)
        )
}