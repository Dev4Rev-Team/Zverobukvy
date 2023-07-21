package ru.gb.zverobukvy.model.data_source

import ru.gb.zverobukvy.model.dto.LetterCard

interface ILetterCardsDB {
    fun readLetterCards(): List<LetterCard>
}