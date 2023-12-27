package ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_players.click_listener_owner

data class PlayerClickListenerOwner(
    val itemPlayerClickListener: (Int) -> Unit,
    val editMenuClickListener: (Int) -> Unit
)