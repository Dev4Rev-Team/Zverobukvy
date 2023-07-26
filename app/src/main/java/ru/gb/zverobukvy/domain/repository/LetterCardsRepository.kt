package ru.gb.zverobukvy.domain.repository

import ru.gb.zverobukvy.domain.entity.LetterCard

interface LetterCardsRepository {
    suspend fun getLetterCards(): List<LetterCard>
}