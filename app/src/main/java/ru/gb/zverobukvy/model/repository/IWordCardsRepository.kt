package ru.gb.zverobukvy.model.repository

import ru.gb.zverobukvy.model.dto.WordCard

interface IWordCardsRepository {
    fun getWordCards(): List<WordCard>
}