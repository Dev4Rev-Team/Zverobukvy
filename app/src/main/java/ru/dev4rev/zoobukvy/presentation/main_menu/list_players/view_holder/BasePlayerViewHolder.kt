package ru.dev4rev.zoobukvy.presentation.main_menu.list_players.view_holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import ru.dev4rev.zoobukvy.presentation.main_menu.PlayerInSettings

abstract class BasePlayerViewHolder(viewBinding: ViewBinding) : ViewHolder(viewBinding.root) {

    abstract fun bindView(playerInSetting: PlayerInSettings?)
}