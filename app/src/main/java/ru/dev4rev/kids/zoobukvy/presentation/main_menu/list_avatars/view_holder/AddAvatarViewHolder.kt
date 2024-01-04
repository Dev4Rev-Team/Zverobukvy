package ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_avatars.view_holder

import android.view.animation.LinearInterpolator
import ru.dev4rev.kids.zoobukvy.R
import ru.dev4rev.kids.zoobukvy.appComponent
import ru.dev4rev.kids.zoobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.dev4rev.kids.zoobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Avatar

class AddAvatarViewHolder(
    private val viewBinding: FragmentMainMenuItemAvatarBinding,
    private val addAvatarsClickListener: () -> Unit
) : BaseAvatarViewHolder(viewBinding) {

    private val imageAvatarLoader: ImageAvatarLoader = itemView.context.appComponent.imageAvatarLoader

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