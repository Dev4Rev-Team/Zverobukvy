package ru.gb.zverobukvy.model.repository

import ru.gb.zverobukvy.model.dto.LetterCard

interface ILetterCardsRepository {
    fun getLetterCards(): List<LetterCard>
}