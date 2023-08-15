package ru.gb.zverobukvy.presentation.main_menu.view.list_players.view_holder

import ru.gb.zverobukvy.databinding.FragmentMainMenuItemPlayerModeEditBinding
import ru.gb.zverobukvy.domain.entity.PlayerInSettings

class EditPlayerViewHolder(
    override val viewBinding: FragmentMainMenuItemPlayerModeEditBinding,
    private val saveChangedPlayerClickListener: (Int, String) -> Unit,
    private val cancelChangedPlayerClickListener: (Int) -> Unit,
    private val queryRemovePlayersClickListener: (Int, String) -> Unit
) :
    BaseViewHolder(viewBinding) {
    override fun bindView(playerInSetting: PlayerInSettings?) {
        playerInSetting?.let {
            viewBinding.run {
                itemEditPlayerInSettingsTextEditPlayerName.setText(playerInSetting.player.name)
                itemEditPlayerInSettingsImageButtonSave.setOnClickListener {
                    saveChangedPlayerClickListener(
                        this@EditPlayerViewHolder.adapterPosition,
                        playerInSetting.player.name
                    )
                }
                itemEditPlayerInSettingsImageButtonCancel.setOnClickListener {
                    cancelChangedPlayerClickListener(this@EditPlayerViewHolder.adapterPosition)
                }
                itemEditPlayerInSettingsImageButtonCancel.setOnClickListener {
                    queryRemovePlayersClickListener(
                        this@EditPlayerViewHolder.adapterPosition,
                        playerInSetting.player.name
                    )
                }
            }
        }
    }
}