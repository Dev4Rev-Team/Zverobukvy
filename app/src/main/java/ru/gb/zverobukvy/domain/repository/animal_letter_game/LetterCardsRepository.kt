package ru.gb.zverobukvy.domain.repository.animal_letter_game

import ru.gb.zverobukvy.domain.entity.card.LetterCard

interface LetterCardsRepository {
    suspend fun getLetterCards(): List<LetterCard>
}