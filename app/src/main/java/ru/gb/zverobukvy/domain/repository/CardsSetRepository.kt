package ru.gb.zverobukvy.domain.repository

import ru.gb.zverobukvy.domain.entity.CardsSet
import ru.gb.zverobukvy.domain.entity.TypeCards

interface CardsSetRepository {
    suspend fun getCardsSet(typeCards: TypeCards): List<CardsSet>
}