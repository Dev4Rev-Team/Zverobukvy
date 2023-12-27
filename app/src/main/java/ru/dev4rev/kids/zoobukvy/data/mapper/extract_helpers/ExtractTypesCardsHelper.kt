package ru.dev4rev.kids.zoobukvy.data.mapper.extract_helpers

import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards

object ExtractTypesCardsHelper {
        private const val COLOR_ORANGE = "orange"
        private const val COLOR_GREEN = "green"
        private const val COLOR_VIOLET = "violet"
        private const val COLOR_BLUE = "blue"

        fun extractColor(typeCards: TypeCards): String =
            when (typeCards) {
                TypeCards.ORANGE -> COLOR_ORANGE
                TypeCards.GREEN -> COLOR_GREEN
                TypeCards.VIOLET -> COLOR_VIOLET
                TypeCards.BLUE -> COLOR_BLUE
            }

        fun extractTypeCards(color: String): TypeCards =
            when (color) {
                COLOR_ORANGE -> TypeCards.ORANGE
                COLOR_GREEN -> TypeCards.GREEN
                COLOR_VIOLET -> TypeCards.VIOLET
                COLOR_BLUE -> TypeCards.BLUE
                else -> TypeCards.ORANGE
            }
}