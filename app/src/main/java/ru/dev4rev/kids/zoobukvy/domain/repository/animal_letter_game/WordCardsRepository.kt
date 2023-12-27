package ru.dev4rev.kids.zoobukvy.domain.repository.animal_letter_game

import ru.dev4rev.kids.zoobukvy.domain.entity.card.WordCard

interface WordCardsRepository {
    suspend fun getWordCards(): List<WordCard>
}