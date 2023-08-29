package ru.gb.zverobukvy.presentation.main_menu.list_players.click_listener_owner

data class EditPlayerClickListenerOwner(
    val saveChangedPlayerClickListener: () -> Unit,
    val cancelChangedPlayerClickListener: () -> Unit,
    val editNameChangedPlayerClickListener: (String) -> Unit,
    val queryRemovePlayersClickListener: (Int) -> Unit
)