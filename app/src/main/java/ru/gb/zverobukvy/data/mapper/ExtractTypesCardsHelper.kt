package ru.gb.zverobukvy.data.mapper

import ru.gb.zverobukvy.data.room.entity.TypeCardsInDatabase
import ru.gb.zverobukvy.domain.entity.TypeCards

class ExtractTypesCardsHelper {
    companion object {
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
    }
}