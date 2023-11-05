package ru.gb.zverobukvy.domain.use_case

import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.WordCard

interface DealCards {
    fun getKitLetterCards(allLetterCards: List<LetterCard>): List<LetterCard>

    fun getKitWordCards(allWordCards: List<WordCard>): List<WordCard>
}