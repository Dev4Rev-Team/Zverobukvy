package ru.dev4rev.kids.zoobukvy.domain.use_case.deal_cards

import ru.dev4rev.kids.zoobukvy.domain.entity.card.LetterCard
import ru.dev4rev.kids.zoobukvy.domain.entity.card.WordCard

interface DealCards {
    fun getKitLetterCards(allLetterCards: List<LetterCard>): List<LetterCard>

    fun getKitWordCards(allWordCards: List<WordCard>): List<WordCard>
}