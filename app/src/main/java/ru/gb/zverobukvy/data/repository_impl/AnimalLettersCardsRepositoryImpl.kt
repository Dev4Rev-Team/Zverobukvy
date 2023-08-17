package ru.gb.zverobukvy.data.repository_impl

import ru.gb.zverobukvy.data.data_source.LetterCardsDB
import ru.gb.zverobukvy.data.data_source.LocalDataSource
import ru.gb.zverobukvy.data.data_source.WordCardsDB
import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.WordCard
import ru.gb.zverobukvy.domain.repository.AnimalLettersCardsRepository
import ru.gb.zverobukvy.domain.repository.PlayersRepository

class AnimalLettersCardsRepositoryImpl (
    private val letterCardsDB: LetterCardsDB,
    private val wordCardsDB: WordCardsDB,
    private val localDataSource: LocalDataSource
) : AnimalLettersCardsRepository, PlayersRepository {
    override suspend fun getLetterCards(): List<LetterCard> =
        letterCardsDB.readLetterCards()

    override suspend fun getWordCards(): List<WordCard> =
        wordCardsDB.readWordCards()

    override suspend fun getPlayers(): List<Player> = localDataSource.getPlayers()

    override suspend fun deletePlayer(player: Player) {
        localDataSource.deletePlayer(player)
    }

    override suspend fun insertPlayers(players: List<Player>) {
        localDataSource.insertPlayers(players)
    }

    override suspend fun insertPlayer(player: Player) {
        localDataSource.insertPlayer(player)
    }

    override suspend fun updatePlayer(player: Player) {
        localDataSource.updatePlayer(player)
    }
}