package ru.gb.zverobukvy.data.resources_provider

import ru.gb.zverobukvy.R

enum class StringEnum(val id: Int) {
    MAIN_MENU_FRAGMENT_NO_CARD_SELECTED(R.string.main_menu_fragment_no_card_selected),
    MAIN_MENU_FRAGMENT_NO_PLAYER_SELECTED(R.string.main_menu_fragment_no_player_selected),
    MAIN_MENU_FRAGMENT_THE_NAME_FIELD_IS_EMPTY(R.string.main_menu_fragment_the_name_field_is_empty),
    MAIN_MENU_FRAGMENT_A_PLAYER_WITH_THE_SAME_NAME_ALREADY_EXISTS(
        R.string.main_menu_fragment_a_player_with_the_same_name_already_exists
    ),
    MAIN_MENU_FRAGMENT_NEW_PLAYER(R.string.main_menu_fragment_new_player),
    MAIN_MENU_FRAGMENT_NO_INTERNET_CONNECTION(R.string.main_menu_fragment_no_internet_connection),
    MAIN_MENU_FRAGMENT_MAX_PLAYERS(R.string.game_view_model_max_players),
    MAIN_MENU_FRAGMENT_NO_SERVER_CONNECTION(R.string.main_menu_fragment_no_server_connection),

    GAME_VIEW_MODEL_TEXT_INVALID_LETTER_MULTIPLAYER(R.string.game_view_model_text_invalid_letters_multiplayer),
    GAME_VIEW_MODEL_TEXT_INVALID_LETTER_SINGLE_PLAYER(R.string.game_view_model_text_invalid_letters_single_player),
    GAME_VIEW_MODEL_TEXT_GUESSED_WORD_MULTIPLAYER(R.string.game_view_model_text_guessed_word_multiplayer),
    GAME_VIEW_MODEL_TEXT_GUESSED_WORD_SINGLE_PLAYER(R.string.game_view_model_text_guessed_word_single_player),
    GAME_VIEW_MODEL_TEXT_DEFAULT_SCREEN_DIMMING(R.string.game_view_model_default_text_for_screen_dimming)
}