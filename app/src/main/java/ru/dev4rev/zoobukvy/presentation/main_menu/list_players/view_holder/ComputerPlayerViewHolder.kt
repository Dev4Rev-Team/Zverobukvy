package ru.dev4rev.zoobukvy.presentation.main_menu.list_players.view_holder

import ru.dev4rev.zoobukvy.R
import ru.dev4rev.zoobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.dev4rev.zoobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl
import ru.dev4rev.zoobukvy.databinding.FragmentMainMenuItemComputerPlayerModeViewBinding
import ru.dev4rev.zoobukvy.presentation.main_menu.PlayerInSettings

class ComputerPlayerViewHolder(
    private val viewBinding: FragmentMainMenuItemComputerPlayerModeViewBinding,
    private val itemPlayerClickListener: (Int) -> Unit
) :
    BasePlayerViewHolder(viewBinding) {

    private val imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl

    override fun bindView(playerInSetting: PlayerInSettings?) {
        playerInSetting?.let {
            viewBinding.run {
                playerNameTextView.text = playerInSetting.player.name
                if (it.isSelectedForGame) {
                    playerStateCardView.setCardBackgroundColor(itemView.context.getColor(R.color.color_green_pastel))
                } else {
                    playerStateCardView.setCardBackgroundColor(itemView.context.getColor(R.color.color_red_pastel))
                }
                playerCardView.setOnClickListener {
                    itemPlayerClickListener(this@ComputerPlayerViewHolder.adapterPosition)
                }
                imageAvatarLoader.loadImageAvatar(it.player.avatar, playerAvatarImageView)
            }
        }
    }
}
