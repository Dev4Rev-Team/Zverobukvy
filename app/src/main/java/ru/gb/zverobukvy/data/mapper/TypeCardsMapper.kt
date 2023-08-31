package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.data.preferences.TypeCardsInSharedPreferences
import ru.gb.zverobukvy.domain.entity.TypeCards

class TypeCardsMapper : EntityMapper<TypeCards, TypeCardsInSharedPreferences> {
    override fun mapToData(entity: TypeCards): TypeCardsInSharedPreferences =
            mapColorFromTypeCard(entity)

    override fun mapToDomain(entity: TypeCardsInSharedPreferences): TypeCards =
            mapTypeCardFromColor(entity.nameTypeCard)

    private fun mapTypeCardFromColor(color: String): TypeCards =
        when (color) {
            COLOR_ORANGE -> TypeCards.ORANGE
            COLOR_GREEN -> TypeCards.GREEN
            COLOR_VIOLET -> TypeCards.VIOLET
            COLOR_BLUE -> TypeCards.BLUE
            else -> TypeCards.ORANGE
        }

    private fun mapColorFromTypeCard(typeCard: TypeCards): TypeCardsInSharedPreferences =
        when (typeCard) {
            TypeCards.ORANGE -> TypeCardsInSharedPreferences(COLOR_ORANGE)
            TypeCards.GREEN -> TypeCardsInSharedPreferences(COLOR_GREEN)
            TypeCards.VIOLET -> TypeCardsInSharedPreferences(COLOR_VIOLET)
            TypeCards.BLUE -> TypeCardsInSharedPreferences(COLOR_BLUE)
        }

    companion object {
        private const val COLOR_ORANGE = "orange"
        private const val COLOR_GREEN = "green"
        private const val COLOR_VIOLET = "violet"
        private const val COLOR_BLUE = "blue"
    }
}