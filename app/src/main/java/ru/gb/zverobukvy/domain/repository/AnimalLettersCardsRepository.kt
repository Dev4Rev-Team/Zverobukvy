package ru.gb.zverobukvy.domain.repository

import ru.gb.zverobukvy.data.data_source_impl.ILetterCardsDB
import ru.gb.zverobukvy.data.data_source_impl.IWordCardsDB
import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.WordCard
import ru.gb.zverobukvy.data.repository_impl.IAnimalLettersCardsRepository

class AnimalLettersCardsRepository(
    private val letterCardsDB: ILetterCardsDB,
    private val wordCardsDB: IWordCardsDB
) : IAnimalLettersCardsRepository {
    override suspend fun getLetterCards(): List<LetterCard> =
        letterCardsDB.readLetterCards()

    override suspend fun getWordCards(): List<WordCard> =
        wordCardsDB.readWordCards()
}