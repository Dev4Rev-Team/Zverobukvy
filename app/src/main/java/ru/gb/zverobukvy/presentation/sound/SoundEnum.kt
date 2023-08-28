package ru.gb.zverobukvy.presentation.sound

enum class SoundEnum(val assetPath: String) {
    CARD_IS_FLIP("sounds/card_is_flip.mp3"),
    CARD_IS_SUCCESSFUL("sounds/card_is_successful.mp3"),
    CARD_IS_UNSUCCESSFUL("sounds/card_is_unsuccessful.mp3"),
    WORD_IS_GUESSED("sounds/word_is_guessed.mp3"),
    GAME_OVER("sounds/game_over.mp3")
}