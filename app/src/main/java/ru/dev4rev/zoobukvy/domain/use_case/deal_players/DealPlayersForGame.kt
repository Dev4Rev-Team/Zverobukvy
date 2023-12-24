package ru.dev4rev.zoobukvy.domain.use_case.deal_players

import ru.dev4rev.zoobukvy.domain.entity.player.Player
import ru.dev4rev.zoobukvy.domain.entity.player.PlayerInGame

interface DealPlayersForGame {
    fun getPlayersForGame(players: List<PlayerInGame>): List<PlayerInGame>

    fun extractHumanPlayers(players: List<Player>): List<Player.HumanPlayer>
}