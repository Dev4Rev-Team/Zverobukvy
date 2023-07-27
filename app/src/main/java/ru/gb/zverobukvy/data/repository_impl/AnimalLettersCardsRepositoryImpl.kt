package ru.gb.zverobukvy.data.repository_impl

import ru.gb.zverobukvy.data.data_source.LetterCardsDB
import ru.gb.zverobukvy.data.data_source.WordCardsDB
import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.WordCard
import ru.gb.zverobukvy.domain.repository.AnimalLettersCardsRepository

class AnimalLettersCardsRepositoryImpl(
    private val letterCardsDB: LetterCardsDB,
    private val wordCardsDB: WordCardsDB
) : AnimalLettersCardsRepository {
    override suspend fun getLetterCards(): List<LetterCard> =
        letterCardsDB.readLetterCards()

    override suspend fun getWordCards(): List<WordCard> =
        wordCardsDB.readWordCards()
}