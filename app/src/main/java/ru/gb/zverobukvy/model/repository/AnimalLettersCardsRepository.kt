package ru.gb.zverobukvy.model.repository

import ru.gb.zverobukvy.model.data_source.ILetterCardsDB
import ru.gb.zverobukvy.model.data_source.IWordCardsDB
import ru.gb.zverobukvy.model.dto.LetterCard
import ru.gb.zverobukvy.model.dto.WordCard

class AnimalLettersCardsRepository(
    private val letterCardsDB: ILetterCardsDB,
    private val wordCardsDB: IWordCardsDB
) : IAnimalLettersCardsRepository {
    override fun getLetterCards(): List<LetterCard> =
        letterCardsDB.readLetterCards()

    override fun getWordCards(): List<WordCard> =
        wordCardsDB.readWordCards()
}