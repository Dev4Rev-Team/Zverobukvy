package ru.gb.zverobukvy.presentation.main_menu.list_players.view_holder

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import coil.decode.SvgDecoder
import coil.load
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemPlayerModeEditBinding
import ru.gb.zverobukvy.presentation.main_menu.PlayerInSettings
import ru.gb.zverobukvy.utility.ui.ExtractAvatarDrawableHelper
import java.nio.ByteBuffer

class EditPlayerViewHolder(
    private val viewBinding: FragmentMainMenuItemPlayerModeEditBinding,
    private val saveChangedPlayerClickListener: () -> Unit,
    private val cancelChangedPlayerClickListener: () -> Unit,
    private val editNameChangedPlayerClickListener: (String) -> Unit,
    private val queryRemovePlayersClickListener: (Int) -> Unit,
    private val avatarPlayerClickListener: () -> Unit,
) :
    BasePlayerViewHolder(viewBinding) {
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
                playerAvatarImageView.apply {
                    val avatar = it.player.avatar
                    if (avatar.isStandard) {
                        viewBinding.playerAvatarImageView.load(
                            ExtractAvatarDrawableHelper.extractDrawable(itemView.context, avatar)
                        )
                    } else {
                        viewBinding.playerAvatarImageView.load(ByteBuffer.wrap(avatar.imageName.toByteArray())) {
                            decoderFactory { result, options, _ ->
                                SvgDecoder(
                                    result.source,
                                    options
                                )
                            }
                        }
                    }
                    isClickable = true
                    setOnClickListener {
                        avatarPlayerClickListener()
                    }
                }
            }
        }
    }
}