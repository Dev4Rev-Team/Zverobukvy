package ru.gb.zverobukvy.data.repository_impl

import ru.gb.zverobukvy.domain.entity.LetterCard

interface ILetterCardsRepository {
    suspend fun getLetterCards(): List<LetterCard>
}