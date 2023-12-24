package ru.dev4rev.zoobukvy.domain.repository.animal_letter_game

import ru.dev4rev.zoobukvy.domain.entity.card.LetterCard

interface LetterCardsRepository {
    suspend fun getLetterCards(): List<LetterCard>
}