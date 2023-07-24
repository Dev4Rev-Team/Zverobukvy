package ru.gb.zverobukvy.data.data_source_impl

import ru.gb.zverobukvy.domain.entity.WordCard

interface IWordCardsDB {
    suspend fun readWordCards(): List<WordCard>
}