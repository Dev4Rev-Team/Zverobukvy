package ru.gb.zverobukvy.data.repository_impl

import ru.gb.zverobukvy.data.data_source.LetterCardsDB
import ru.gb.zverobukvy.data.data_source.WordCardsDB
import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.WordCard
import ru.gb.zverobukvy.domain.repository.AnimalLettersCardsRepository
import ru.gb.zverobukvy.domain.repository.PlayersRepository

class AnimalLettersCardsRepositoryImpl(
    private val letterCardsDB: LetterCardsDB,
    private val wordCardsDB: WordCardsDB
) : AnimalLettersCardsRepository, PlayersRepository {
    override suspend fun getLetterCards(): List<LetterCard> =
        letterCardsDB.readLetterCards()

    override suspend fun getWordCards(): List<WordCard> =
        wordCardsDB.readWordCards()

    override suspend fun getPlayers(namesPlayers: List<String>): List<Player> =
        //TODO реализовать запрос в БД
        listOf(Player("Игрок 1"), Player("Игрок 2"), Player("Игрок 3"))
}