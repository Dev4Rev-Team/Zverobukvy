package ru.gb.zverobukvy.domain.repository

import ru.gb.zverobukvy.domain.entity.TypeCards

interface TypesCardsSelectedForGameRepository {
    fun getTypesCardsSelectedForGame(): List<TypeCards>

    fun saveTypesCardsSelectedForGame(typesCardsSelectedForGame: List<TypeCards>)
}