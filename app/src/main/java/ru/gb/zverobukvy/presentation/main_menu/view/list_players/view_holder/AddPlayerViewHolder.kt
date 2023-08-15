package ru.gb.zverobukvy.presentation.main_menu.view.list_players.view_holder

import ru.gb.zverobukvy.databinding.ItemAddPlayerInSettingsBinding
import ru.gb.zverobukvy.domain.entity.PlayerInSettings

class AddPlayerViewHolder(
    override val viewBinding: ItemAddPlayerInSettingsBinding,
    private val addPlayerClickListener: () -> Unit) :
    BaseViewHolder(viewBinding) {
    override fun bindView(playerInSetting: PlayerInSettings?) {
        viewBinding.addPlayerFab.setOnClickListener {
            addPlayerClickListener()
        }
    }
}