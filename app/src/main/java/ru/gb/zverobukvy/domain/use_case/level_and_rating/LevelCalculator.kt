package ru.gb.zverobukvy.domain.use_case.level_and_rating

import ru.gb.zverobukvy.domain.entity.player.Player

interface LevelCalculator {

    fun updateLettersGuessingLevel(player: Player.HumanPlayer, isCorrectStep: Boolean)

    fun getUpdatedPlayers(): List<Player.HumanPlayer>
}