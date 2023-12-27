package ru.dev4rev.kids.zoobukvy.presentation.sound

enum class SoundEnum(val assetPath: String) {
    CARD_IS_FLIP("card_is_flip.mp3"),
    CARD_IS_SUCCESSFUL("card_is_successful.mp3"),
    CARD_IS_UNSUCCESSFUL("card_is_unsuccessful.mp3"),
    WORD_IS_GUESSED("word_is_guessed.mp3"),
    GAME_OVER("game_over.mp3"),
    AWARD_SCREEN_INIT("award_screen_init.mp3"),
    RANK_INCREASE("rank_increase.mp3"),
    VIEW_RATING_INCREASE("view_rating_increase.mp3"),
    NEW_AWARDED_PLAYER("new_awarded_player.mp3")
}