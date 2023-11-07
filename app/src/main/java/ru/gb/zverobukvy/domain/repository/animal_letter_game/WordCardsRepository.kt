package ru.gb.zverobukvy.domain.repository.animal_letter_game

import ru.gb.zverobukvy.domain.entity.card.WordCard

interface WordCardsRepository {
    suspend fun getWordCards(): List<WordCard>
}