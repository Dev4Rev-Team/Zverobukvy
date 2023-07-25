package ru.gb.zverobukvy.domain.entity

data class GameState(
    val gameField: GameField,
    val players: List<Player>,
    var walkingPlayer: Player?,
    var isActive: Boolean
)
