package ru.gb.zverobukvy.data.data_source

import ru.gb.zverobukvy.domain.entity.Player

interface LocalDataSource {
    suspend fun getPlayers(): List<Player>

    suspend fun deletePlayer(player: Player)

    suspend fun insertPlayers(players: List<Player>)

    suspend fun insertPlayer(player: Player)

    suspend fun updatePlayer(player: Player)
}