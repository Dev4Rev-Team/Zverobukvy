package ru.dev4rev.kids.zoobukvy.domain.entity.game_state

enum class GameStateName {
    START_GAME, WRONG_LETTER_CARD, NEXT_WALKING_PLAYER, NOT_LAST_CORRECT_LETTER_CARD,
    GUESSED_NOT_LAST_WORD_CARD, NEXT_WORD_CARD, END_GAME, END_GAME_BY_USER, UPDATE_LETTER_CARD
}