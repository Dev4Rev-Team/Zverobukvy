package ru.gb.zverobukvy.data.data_source

import ru.gb.zverobukvy.domain.entity.WordCard

interface WordCardsDB {
    suspend fun readWordCards(): List<WordCard>
}