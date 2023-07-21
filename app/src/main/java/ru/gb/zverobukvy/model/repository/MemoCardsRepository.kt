package ru.gb.zverobukvy.model.repository

import ru.gb.zverobukvy.model.data_source.ILetterCardsDB
import ru.gb.zverobukvy.model.dto.LetterCard

class MemoCardsRepository(
    private val letterCardsDB: ILetterCardsDB
) : IMemoCardsRepository {
    override fun getLetterCards(): List<LetterCard> =
        letterCardsDB.readLetterCards()
}