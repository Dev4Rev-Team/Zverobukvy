package ru.dev4rev.kids.zoobukvy.domain.use_case.calculate_color_letters

import ru.dev4rev.kids.zoobukvy.domain.entity.card.LetterCard

interface CalculateColorLetters {
    fun calculate(word: String, letterCards: List<LetterCard>)
}

