package ru.gb.zverobukvy.presentation.main_menu.list_players.view_holder

import ru.gb.zverobukvy.databinding.ItemEditPlayerInSettingsBinding
import ru.gb.zverobukvy.domain.entity.PlayerInSettings

class EditPlayerViewHolder(
    override val viewBinding: ItemEditPlayerInSettingsBinding,
    private val saveChangedPlayerClickListener: (Int, String) -> Unit,
    private val cancelChangedPlayerClickListener: (Int) -> Unit,
    private val queryRemovePlayersClickListener: (Int) -> Unit
) :
    BaseViewHolder(viewBinding) {
    override fun bindView(playerInSetting: PlayerInSettings?) {
        playerInSetting?.let {
            viewBinding.run {
                namePlayerEditText.setText(playerInSetting.player.name)
                saveChangedPlayerButton.setOnClickListener {
                    saveChangedPlayerClickListener(
                        this@EditPlayerViewHolder.adapterPosition,
                        namePlayerEditText.text.toString()
                    )
                }
                cancelChangedPlayerButton.setOnClickListener {
                    cancelChangedPlayerClickListener(this@EditPlayerViewHolder.adapterPosition)
                }
                removePlayerFab.setOnClickListener {
                    queryRemovePlayersClickListener(this@EditPlayerViewHolder.adapterPosition)
                }
            }
        }
    }
}