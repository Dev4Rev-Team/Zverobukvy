package ru.gb.zverobukvy.presentation.main_menu.list_players.view_holder

import android.text.Editable
import android.text.TextWatcher
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemPlayerModeEditBinding
import ru.gb.zverobukvy.presentation.main_menu.PlayerInSettings

class EditPlayerViewHolder(
    override val viewBinding: FragmentMainMenuItemPlayerModeEditBinding,
    private val saveChangedPlayerClickListener: () -> Unit,
    private val cancelChangedPlayerClickListener: () -> Unit,
    private val editNameChangedPlayerClickListener: (String) -> Unit,
    private val queryRemovePlayersClickListener: (Int, String) -> Unit
) :
    BaseViewHolder(viewBinding) {
    override fun bindView(playerInSetting: PlayerInSettings?) {
        playerInSetting?.let {
            viewBinding.run {
                playerNameTextInputView.setText(playerInSetting.player.name)
                saveImageButton.setOnClickListener {
                    saveChangedPlayerClickListener()
                }
                cancelImageButton.setOnClickListener {
                    cancelChangedPlayerClickListener()
                }
                playerNameTextInputView.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        editNameChangedPlayerClickListener(s.toString())
                    }

                })
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