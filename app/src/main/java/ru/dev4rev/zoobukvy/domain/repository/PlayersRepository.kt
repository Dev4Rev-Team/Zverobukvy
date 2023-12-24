package ru.dev4rev.zoobukvy.domain.repository

import ru.dev4rev.zoobukvy.domain.entity.player.Player

interface PlayersRepository {
    suspend fun getPlayers(): List<Player>

    suspend fun deletePlayer(player: Player)
    
    suspend fun insertPlayer(player: Player): Long

    suspend fun updatePlayer(player: Player)
}