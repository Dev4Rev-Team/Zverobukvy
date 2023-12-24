package ru.dev4rev.zoobukvy.presentation.main_menu.list_avatars.view_holder

import ru.dev4rev.zoobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.dev4rev.zoobukvy.domain.entity.player.Avatar
import ru.dev4rev.zoobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.dev4rev.zoobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl

class AvatarViewHolder(
    private val viewBinding: FragmentMainMenuItemAvatarBinding,
    private val avatarClickListener: (Int) -> Unit,
) : BaseAvatarViewHolder(viewBinding) {

    private var imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl

    override fun bindView(avatar: Avatar) {
        imageAvatarLoader.loadImageAvatar(avatar, viewBinding.playerAvatarImageView)
        viewBinding.avatarCardView.setOnClickListener {
            avatarClickListener(adapterPosition)
        }
    }
}