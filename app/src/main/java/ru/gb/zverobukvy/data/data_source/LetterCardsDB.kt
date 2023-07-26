package ru.gb.zverobukvy.data.data_source

import ru.gb.zverobukvy.domain.entity.LetterCard

interface LetterCardsDB {
    suspend fun readLetterCards(): List<LetterCard>
}