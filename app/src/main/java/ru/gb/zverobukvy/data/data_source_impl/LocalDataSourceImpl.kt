package ru.gb.zverobukvy.data.data_source_impl

import ru.gb.zverobukvy.data.data_source.LocalDataSource
import ru.gb.zverobukvy.data.room.PlayersDatabase
import ru.gb.zverobukvy.domain.entity.Player

class LocalDataSourceImpl (playersDatabase: PlayersDatabase): LocalDataSource {
    private val playersDao = playersDatabase.playersDao()

    override suspend fun getPlayers(): List<Player> = playersDao.getPlayers()

    override suspend fun deletePlayer(player: Player) {
        playersDao.deletePlayer(player)
    }

    override suspend fun insertPlayers(players: List<Player>) {
        playersDao.insertPlayers(players)
    }

    override suspend fun insertPlayer(player: Player) {
        playersDao.insertPlayer(player)
    }

    override suspend fun updatePlayer(player: Player) {
        playersDao.updatePlayer(player)
    }
}