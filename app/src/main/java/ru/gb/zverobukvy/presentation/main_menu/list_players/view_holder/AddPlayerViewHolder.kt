package ru.gb.zverobukvy.presentation.main_menu.list_players.view_holder

import ru.gb.zverobukvy.databinding.FragmentMainMenuItemPlayerModeAddBinding
import ru.gb.zverobukvy.presentation.main_menu.PlayerInSettings

class AddPlayerViewHolder(
    private val viewBinding: FragmentMainMenuItemPlayerModeAddBinding,
    private val addPlayerClickListener: () -> Unit) :
    BasePlayerViewHolder(viewBinding) {
    override fun bindView(playerInSetting: PlayerInSettings?) {
        viewBinding.addPlayerCardView.setOnClickListener {
            addPlayerClickListener()
        }
    }
}