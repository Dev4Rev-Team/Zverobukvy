package ru.dev4rev.kids.zoobukvy.domain.entity.game_state

import ru.dev4rev.kids.zoobukvy.domain.entity.player.PlayerInGame
import ru.dev4rev.kids.zoobukvy.domain.entity.sound.VoiceActingStatus

data class GameState(
    val name: GameStateName,
    val gameField: GameField,
    val players: List<PlayerInGame>,
    val walkingPlayer: PlayerInGame?,
    val nextWalkingPlayer: PlayerInGame?,
    val isActive: Boolean,
    val voiceActingStatus: VoiceActingStatus
)
