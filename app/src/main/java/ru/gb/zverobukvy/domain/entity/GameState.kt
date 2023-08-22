package ru.gb.zverobukvy.domain.entity

data class GameState(
    val gameField: GameField,
    val players: List<PlayerInGame>,
    var walkingPlayer: PlayerInGame?,
    var isActive: Boolean
)