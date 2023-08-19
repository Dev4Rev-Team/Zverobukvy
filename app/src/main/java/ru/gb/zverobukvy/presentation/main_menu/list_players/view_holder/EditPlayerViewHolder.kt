package ru.gb.zverobukvy.presentation.main_menu.list_players.view_holder

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
                playerNameTextInputView.setText(playerInSetting.player.name)
                saveImageButton.setOnClickListener {
                    saveChangedPlayerClickListener(
                        this@EditPlayerViewHolder.adapterPosition,
                        playerNameTextInputView.text.toString()
                    )
                }
//               TODO  cancelImageButton.setOnClickListener {
//                    cancelChangedPlayerClickListener(this@EditPlayerViewHolder.adapterPosition)
//                }
                deleteButton.setOnClickListener {
                    queryRemovePlayersClickListener(
                        this@EditPlayerViewHolder.adapterPosition,
                        playerNameTextInputView.text.toString()
                    )
                }
            }
        }
    }
}