package ru.dev4rev.zoobukvy.domain.use_case.deal_players

import ru.dev4rev.zoobukvy.domain.entity.player.Player
import ru.dev4rev.zoobukvy.domain.entity.player.PlayerInGame

class DealPlayersForGameImpl: DealPlayersForGame {
    /**
     * Метод перемешивает игроков и игрока-компьютер отправляет в конец списка
     * @param players список игроков
     * @return список игроков
     */
    override fun getPlayersForGame(players: List<PlayerInGame>): List<PlayerInGame> {
        val playersForGame = players.shuffled().toMutableList()
        val computer = playersForGame.find {
            it.player is Player.ComputerPlayer
        }
        computer?.let {
            with(playersForGame) {
                if (indexOf(it) != size - 1) {
                    remove(it)
                    add(it)
                }
            }
        }
        return playersForGame.toList()
    }

    /**
     * Метод создает из списка Player список только HumanPlayer
     */
    override fun extractHumanPlayers(players: List<Player>): List<Player.HumanPlayer> =
        mutableListOf<Player.HumanPlayer>().apply {
            players.forEach {
                if (it is Player.HumanPlayer)
                    add(it)
            }
        }
}