package ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_players.click_listener_owner

class EditPlayerClickListenerOwner(
    val saveChangedPlayerClickListener: () -> Unit,
    val cancelChangedPlayerClickListener: () -> Unit,
    val editNameChangedPlayerClickListener: (String) -> Unit,
    val queryRemovePlayersClickListener: (Int) -> Unit,
    val avatarPlayerClickListener: () -> Unit
)