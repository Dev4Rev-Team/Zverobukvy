package ru.dev4rev.kids.zoobukvy.domain.repository.animal_letter_game

import ru.dev4rev.kids.zoobukvy.domain.entity.card.CardsSet
import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards

interface CardsSetRepository {
    suspend fun getCardsSet(typeCards: TypeCards): List<CardsSet>
}