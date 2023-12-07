package ru.gb.zverobukvy.domain.use_case.deal_players

import ru.gb.zverobukvy.domain.entity.player.Player
import ru.gb.zverobukvy.domain.entity.player.PlayerInGame

interface DealPlayersForGame {
    fun getPlayersForGame(players: List<PlayerInGame>): List<PlayerInGame>

    fun extractHumanPlayers(players: List<Player>): List<Player.HumanPlayer>
}