package ru.gb.zverobukvy.presentation.main_menu.list_avatars.view_holder

import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.gb.zverobukvy.domain.entity.Avatar
import ru.gb.zverobukvy.utility.ui.image_avatar_loader.ImageAvatarLoader
import ru.gb.zverobukvy.utility.ui.image_avatar_loader.ImageAvatarLoaderImpl

class AddAvatarViewHolder(
    private val viewBinding: FragmentMainMenuItemAvatarBinding,
    private val addAvatarsClickListener: () -> Unit
) : BaseAvatarViewHolder(viewBinding) {

   private var imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl

    override fun bindView(avatar: Avatar) {
        viewBinding.run {
            imageAvatarLoader.loadImageAvatar(avatar, playerAvatarImageView)
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