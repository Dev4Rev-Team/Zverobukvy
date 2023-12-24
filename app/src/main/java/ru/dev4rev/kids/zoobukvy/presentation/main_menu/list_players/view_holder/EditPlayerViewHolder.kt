package ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_players.view_holder

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import ru.dev4rev.kids.zoobukvy.databinding.FragmentMainMenuItemPlayerModeEditBinding
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.PlayerInSettings
import ru.dev4rev.kids.zoobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.dev4rev.kids.zoobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl

class EditPlayerViewHolder(
    private val viewBinding: FragmentMainMenuItemPlayerModeEditBinding,
    private val saveChangedPlayerClickListener: () -> Unit,
    private val cancelChangedPlayerClickListener: () -> Unit,
    private val editNameChangedPlayerClickListener: (String) -> Unit,
    private val queryRemovePlayersClickListener: (Int) -> Unit,
    private val avatarPlayerClickListener: () -> Unit,
) :
    BasePlayerViewHolder(viewBinding) {

    private val imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl

    override fun bindView(playerInSetting: PlayerInSettings?) {
        playerInSetting?.let {
            viewBinding.run {
                playerNameTextInputView.imeOptions = EditorInfo.IME_ACTION_DONE
                playerNameTextInputView.setText(playerInSetting.player.name)
                saveImageButton.setOnClickListener {
                    saveChangedPlayerClickListener()
                }
                cancelImageButton.setOnClickListener {
                    cancelChangedPlayerClickListener()
                }
                playerNameTextInputView.addTextChangedListener(object : TextWatcher {
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
                        this@EditPlayerViewHolder.adapterPosition
                    )
                }

                imageAvatarLoader.loadImageAvatar(it.player.avatar, playerAvatarImageView)

                playerAvatarImageView.apply {
                    isClickable = true
                    setOnClickListener {
                        avatarPlayerClickListener()
                    }
                }
            }
        }
    }
}