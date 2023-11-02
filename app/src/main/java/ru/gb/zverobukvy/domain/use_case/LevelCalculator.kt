package ru.gb.zverobukvy.domain.use_case

import ru.gb.zverobukvy.domain.entity.Player

interface LevelCalculator {

    fun updateLettersGuessingLevel(player: Player, isCorrectStep: Boolean)

    fun getUpdatedPlayers(): List<Player.HumanPlayer>
}