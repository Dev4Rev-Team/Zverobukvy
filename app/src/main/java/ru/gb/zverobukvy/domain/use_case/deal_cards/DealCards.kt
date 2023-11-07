package ru.gb.zverobukvy.domain.use_case.deal_cards

import ru.gb.zverobukvy.domain.entity.card.LetterCard
import ru.gb.zverobukvy.domain.entity.card.WordCard

interface DealCards {
    fun getKitLetterCards(allLetterCards: List<LetterCard>): List<LetterCard>

    fun getKitWordCards(allWordCards: List<WordCard>): List<WordCard>
}