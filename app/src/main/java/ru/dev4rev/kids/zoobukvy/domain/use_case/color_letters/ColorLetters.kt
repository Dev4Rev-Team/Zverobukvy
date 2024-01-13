package ru.dev4rev.kids.zoobukvy.domain.use_case.color_letters

import ru.dev4rev.kids.zoobukvy.domain.entity.card.LetterCard

interface ColorLetters {
    fun setColor(word: String, letters: List<LetterCard>)
}

