package ru.gb.zverobukvy.presentation.main_menu.list_avatars.view_holder

import android.view.animation.LinearInterpolator
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.gb.zverobukvy.domain.entity.Avatar
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl

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
                it.isClickable = false
                playerAvatarImageView.animate()
                    .rotation(ROTATION)
                    .setInterpolator(LinearInterpolator())
                    .setDuration(DURATION)
                    .start()
                addAvatarsClickListener()
            }
        }
    }

    companion object {
        private const val ROTATION = 32400F
        private const val DURATION = 60000L
    }
}