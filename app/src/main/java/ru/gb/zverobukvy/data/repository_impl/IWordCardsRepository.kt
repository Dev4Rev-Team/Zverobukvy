package ru.gb.zverobukvy.data.repository_impl

import ru.gb.zverobukvy.domain.entity.WordCard

interface IWordCardsRepository {
    suspend fun getWordCards(): List<WordCard>
}