package ru.gb.zverobukvy.presentation.main_menu.list_avatars.view_holder

import coil.load
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.gb.zverobukvy.domain.entity.Avatar
import ru.gb.zverobukvy.utility.ui.ExtractAvatarDrawableHelper

class AddAvatarViewHolder(
    private val viewBinding: FragmentMainMenuItemAvatarBinding,
    private val addAvatarsClickListener: () -> Unit
) : BaseAvatarViewHolder(viewBinding) {
    override fun bindView(avatar: Avatar) {
        viewBinding.run {
            playerAvatarImageView.load(
                ExtractAvatarDrawableHelper.extractDrawable(itemView.context, avatar)
            )
            backgroundAvatarCardView.apply {
                setCardBackgroundColor(itemView.context.getColor(R.color.white))
                backgroundAvatarCardView.elevation = 0F
            }
            avatarCardView.setOnClickListener {
                addAvatarsClickListener()
            }
        }
    }
}