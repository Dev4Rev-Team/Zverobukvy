package ru.gb.zverobukvy.domain.repository

import ru.gb.zverobukvy.domain.entity.WordCard

interface WordCardsRepository {
    suspend fun getWordCards(): List<WordCard>
}