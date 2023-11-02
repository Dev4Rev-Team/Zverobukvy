package ru.gb.zverobukvy.presentation.sound

enum class SoundEnum(val assetPath: String) {
    CARD_IS_FLIP("card_is_flip.mp3"),
    CARD_IS_SUCCESSFUL("card_is_successful.mp3"),
    CARD_IS_UNSUCCESSFUL("card_is_unsuccessful.mp3"),
    WORD_IS_GUESSED("word_is_guessed.mp3"),
    GAME_OVER("game_over.mp3")
}