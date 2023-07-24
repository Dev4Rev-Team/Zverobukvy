package ru.gb.zverobukvy.data.data_source_impl

import ru.gb.zverobukvy.domain.entity.LetterCard

interface ILetterCardsDB {
    suspend fun readLetterCards(): List<LetterCard>
}