package ru.dev4rev.kids.zoobukvy.domain.repository.main_menu.shared_preferences

import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards

interface TypesCardsSelectedForGameRepository {
    fun getTypesCardsSelectedForGame(): List<TypeCards>

    fun saveTypesCardsSelectedForGame(typesCardsSelectedForGame: List<TypeCards>)
}