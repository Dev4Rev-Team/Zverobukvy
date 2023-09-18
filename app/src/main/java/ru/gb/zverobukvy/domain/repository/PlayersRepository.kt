package ru.gb.zverobukvy.domain.repository

import ru.gb.zverobukvy.domain.entity.Player

interface PlayersRepository {
    suspend fun getPlayers(): List<Player.HumanPlayer>

    suspend fun deletePlayer(player: Player.HumanPlayer)
    
    suspend fun insertPlayer(player: Player.HumanPlayer): Long

    suspend fun updatePlayer(player: Player.HumanPlayer)

}