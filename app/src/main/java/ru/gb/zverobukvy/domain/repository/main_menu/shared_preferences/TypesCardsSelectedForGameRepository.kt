package ru.gb.zverobukvy.domain.repository.main_menu.shared_preferences

import ru.gb.zverobukvy.domain.entity.card.TypeCards

interface TypesCardsSelectedForGameRepository {
    fun getTypesCardsSelectedForGame(): List<TypeCards>

    fun saveTypesCardsSelectedForGame(typesCardsSelectedForGame: List<TypeCards>)
}