package ru.dev4rev.zoobukvy.domain.repository.animal_letter_game

import ru.dev4rev.zoobukvy.domain.entity.card.WordCard

interface WordCardsRepository {
    suspend fun getWordCards(): List<WordCard>
}