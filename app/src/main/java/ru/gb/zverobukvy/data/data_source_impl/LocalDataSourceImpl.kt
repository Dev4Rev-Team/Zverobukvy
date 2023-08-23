package ru.gb.zverobukvy.data.data_source_impl

import ru.gb.zverobukvy.data.data_source.LocalDataSource
import ru.gb.zverobukvy.data.room.AnimalLettersDatabase
import ru.gb.zverobukvy.data.room.entity.LetterCardInDatabase
import ru.gb.zverobukvy.data.room.entity.WordCardInDatabase
import ru.gb.zverobukvy.domain.entity.Player
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val playersDatabase: AnimalLettersDatabase,
) : LocalDataSource {
    private val playersDao = playersDatabase.playersDao()

    override suspend fun getPlayers(): List<Player> = playersDao.getPlayers()

    override suspend fun deletePlayer(player: Player) {
        playersDao.deletePlayer(player)
    }

    override suspend fun insertPlayer(player: Player) {
        playersDao.insertPlayer(player)
    }

    override suspend fun updatePlayer(player: Player) {
        playersDao.updatePlayer(player)
    }

    override suspend fun getLetterCards(): List<LetterCardInDatabase> =
        playersDatabase.letterCardsDao().getLetterCards()

    override suspend fun getWordCards(): List<WordCardInDatabase> =
        playersDatabase.wordCardsDao().getWordCards()

}