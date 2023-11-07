package ru.gb.zverobukvy.domain.repository.animal_letter_game

import ru.gb.zverobukvy.domain.entity.card.CardsSet
import ru.gb.zverobukvy.domain.entity.card.TypeCards

interface CardsSetRepository {
    suspend fun getCardsSet(typeCards: TypeCards): List<CardsSet>
}