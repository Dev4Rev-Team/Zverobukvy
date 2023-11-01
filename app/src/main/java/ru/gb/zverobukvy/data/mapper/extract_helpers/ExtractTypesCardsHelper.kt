package ru.gb.zverobukvy.data.mapper.extract_helpers

import ru.gb.zverobukvy.data.room.entity.TypeCardsInDatabase
import ru.gb.zverobukvy.domain.entity.TypeCards

object ExtractTypesCardsHelper {
        private const val COLOR_ORANGE = "orange"
        private const val COLOR_GREEN = "green"
        private const val COLOR_VIOLET = "violet"
        private const val COLOR_BLUE = "blue"

        fun extractTypesCards(typeCardsInDatabase: TypeCardsInDatabase): List<TypeCards> {
            val typeCards = mutableListOf<TypeCards>()
            typeCardsInDatabase.run {
                if (isOrange)
                    typeCards.add(TypeCards.ORANGE)
                if (isGreen)
                    typeCards.add(TypeCards.GREEN)
                if (isBlue)
                    typeCards.add(TypeCards.BLUE)
                if (isViolet)
                    typeCards.add(TypeCards.VIOLET)
            }
            return typeCards.toList()
        }

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