package ru.dev4rev.zoobukvy.presentation.main_menu.list_players.view_holder

import ru.dev4rev.zoobukvy.databinding.FragmentMainMenuItemPlayerModeAddBinding
import ru.dev4rev.zoobukvy.presentation.main_menu.PlayerInSettings

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