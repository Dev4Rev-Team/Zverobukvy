package ru.gb.zverobukvy.presentation.main_menu.list_players.click_listener_owner

data class EditPlayerClickListenerOwner(
    val saveChangedPlayerClickListener: (Int, String) -> Unit,
    val cancelChangedPlayerClickListener: (Int) -> Unit,
    val queryRemovePlayersClickListener: (Int, String) -> Unit
)