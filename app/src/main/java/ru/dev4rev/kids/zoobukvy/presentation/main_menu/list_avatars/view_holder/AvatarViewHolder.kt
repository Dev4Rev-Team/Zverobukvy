package ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_avatars.view_holder

import ru.dev4rev.kids.zoobukvy.appComponent
import ru.dev4rev.kids.zoobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.dev4rev.kids.zoobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Avatar

class AvatarViewHolder(
    private val viewBinding: FragmentMainMenuItemAvatarBinding,
    private val avatarClickListener: (Int) -> Unit,
) : BaseAvatarViewHolder(viewBinding) {

    private val imageAvatarLoader: ImageAvatarLoader = itemView.context.appComponent.imageAvatarLoader

    override fun bindView(avatar: Avatar) {
        imageAvatarLoader.loadImageAvatar(avatar, viewBinding.playerAvatarImageView)
        viewBinding.avatarCardView.setOnClickListener {
            avatarClickListener(adapterPosition)
        }
    }
}