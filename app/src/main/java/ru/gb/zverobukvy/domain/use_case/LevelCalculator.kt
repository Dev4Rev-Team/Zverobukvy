package ru.gb.zverobukvy.domain.use_case

import ru.gb.zverobukvy.domain.entity.Player

interface LevelCalculator {

    fun updateLettersGuessingLevel(idPlayer: Long, isCorrectStep: Boolean)

    fun getPlayersWithActualLevel(): List<Player.HumanPlayer>
}