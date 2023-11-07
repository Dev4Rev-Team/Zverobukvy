package ru.gb.zverobukvy.presentation.main_menu.list_avatars.view_holder

import ru.gb.zverobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.gb.zverobukvy.domain.entity.player.Avatar
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl

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