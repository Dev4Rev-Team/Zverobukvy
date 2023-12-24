package ru.dev4rev.zoobukvy.domain.entity.game_state

import ru.dev4rev.zoobukvy.domain.entity.player.PlayerInGame

data class GameState(
    val gameField: GameField,
    val players: List<PlayerInGame>,
    var walkingPlayer: PlayerInGame?,
    var nextWalkingPlayer: PlayerInGame?,
    var isActive: Boolean
)
