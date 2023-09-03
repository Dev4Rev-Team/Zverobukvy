package ru.gb.zverobukvy.presentation.main_menu.list_players.view_holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import ru.gb.zverobukvy.presentation.main_menu.PlayerInSettings

abstract class BasePlayerViewHolder(viewBinding: ViewBinding) : ViewHolder(viewBinding.root) {
    abstract fun bindView(playerInSetting: PlayerInSettings?)
}