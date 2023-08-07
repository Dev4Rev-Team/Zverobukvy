package ru.gb.zverobukvy.domain.use_case

import ru.gb.zverobukvy.domain.entity.Card
import ru.gb.zverobukvy.domain.entity.TypeCards

class DealCards {
    companion object {
        /**
        Метод формирует набор карточек для игры на основе полного набора карточек и уровня игры
        (цвета игры): отбирает подходящие карточки и перемешивает их.
         * @param allCards список всех карточек
         * @param typesCards список типов карточек (цвета)
         * @return список выбранных карточек для игры
         */
        fun <T : Card> getKitCards(
            allCards: List<T>,
            typesCards: List<TypeCards>
        ): List<T> {
            val kitCards = mutableListOf<T>()
            for (i in allCards.indices) {
                allCards[i].typesCards.forEach {
                    if (typesCards.contains(it)) {
                        if(kitCards.contains(allCards[i]))
                            return@forEach
                        kitCards.add(allCards[i])
                    }
                }
            }
            return kitCards.shuffled()
        }
    }
}