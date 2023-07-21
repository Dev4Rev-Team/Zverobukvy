package ru.gb.zverobukvy.model.data_source

import ru.gb.zverobukvy.model.dto.WordCard

interface IWordCardsDB {
    fun readWordCards(): List<WordCard>
}