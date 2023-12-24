package ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.card

import ru.dev4rev.kids.zoobukvy.data.mapper.EntityMapperToDomain
import ru.dev4rev.kids.zoobukvy.data.mapper.extract_helpers.ExtractLettersHelper
import ru.dev4rev.kids.zoobukvy.data.mapper.extract_helpers.ExtractTypesCardsHelper
import ru.dev4rev.kids.zoobukvy.data.mapper.extract_helpers.ExtractWordsHelper
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.CardsSetInDatabase
import ru.dev4rev.kids.zoobukvy.domain.entity.card.CardsSet

class CardsSetMapperToDomain: EntityMapperToDomain<CardsSet, CardsSetInDatabase> {
    override fun mapToDomain(entity: CardsSetInDatabase): CardsSet =
        CardsSet(
            ExtractTypesCardsHelper.extractTypeCards(entity.color),
            ExtractLettersHelper.extractLetters(entity.letters),
            ExtractWordsHelper.extractWords(entity.words)
        )
}