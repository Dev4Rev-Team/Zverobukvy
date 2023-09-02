package ru.gb.zverobukvy.presentation.main_menu.list_avatars

import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.gb.zverobukvy.domain.entity.Avatar
import ru.gb.zverobukvy.utility.ui.ExtractAvatarDrawableHelper

class AvatarViewHolder(
    private val viewBinding: FragmentMainMenuItemAvatarBinding,
    private val avatarClickListener: (Int) -> Unit
) :
    RecyclerView.ViewHolder(viewBinding.root) {
    fun binding(avatar: Avatar) {
        viewBinding.playerAvatarImageView.load(
           ExtractAvatarDrawableHelper.extractDrawable(itemView.context, avatar)
        )
        viewBinding.avatarCardView.setOnClickListener {
            avatarClickListener(adapterPosition)
        }
    }
}