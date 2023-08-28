package ru.gb.zverobukvy.presentation.main_menu.list_players.view_holder

import ru.gb.zverobukvy.databinding.FragmentMainMenuItemPlayerModeAddBinding
import ru.gb.zverobukvy.presentation.main_menu.PlayerInSettings

class AddPlayerViewHolder(
    override val viewBinding: FragmentMainMenuItemPlayerModeAddBinding,
    private val addPlayerClickListener: () -> Unit) :
    BaseViewHolder(viewBinding) {
    override fun bindView(playerInSetting: PlayerInSettings?) {
        viewBinding.addPlayerCardView.setOnClickListener {
            addPlayerClickListener()
        }
    }
}