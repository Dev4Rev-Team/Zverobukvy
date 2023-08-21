package ru.gb.zverobukvy.domain.repository

import ru.gb.zverobukvy.domain.entity.Player

interface PlayersRepository {
    suspend fun getPlayers(): List<Player>

    suspend fun deletePlayer(player: Player)
    
    suspend fun insertPlayer(player: Player)

    suspend fun updatePlayer(player: Player)

}