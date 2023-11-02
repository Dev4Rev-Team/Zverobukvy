package ru.gb.zverobukvy.presentation.main_menu.list_players.view_holder

import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemPlayerModeViewBinding
import ru.gb.zverobukvy.presentation.main_menu.PlayerInSettings
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl
import timber.log.Timber

class PlayerViewHolder(
    private val viewBinding: FragmentMainMenuItemPlayerModeViewBinding,
    private val itemPlayerClickListener: (Int) -> Unit,
    private val editMenuClickListener: (Int) -> Unit
) :
    BasePlayerViewHolder(viewBinding) {

    private var imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl

    override fun bindView(playerInSetting: PlayerInSettings?) {
        playerInSetting?.let {
            viewBinding.run {
                playerNameTextView.text = it.player.name
                if (it.isSelectedForGame) {
                    playerStateCardView.setCardBackgroundColor(itemView.context.getColor(R.color.color_green_pastel))
                } else {
                    playerStateCardView.setCardBackgroundColor(itemView.context.getColor(R.color.color_red_pastel))
                }
                playerCardView.setOnClickListener {
                    itemPlayerClickListener(this@PlayerViewHolder.adapterPosition)
                }
                editImageButton.setOnClickListener {
                    editMenuClickListener(this@PlayerViewHolder.adapterPosition)
                }
                imageAvatarLoader.loadImageAvatar(it.player.avatar, playerAvatarImageView)
                Timber.d("${playerInSetting.player.name} orangeLevel ${playerInSetting.player.lettersGuessingLevel.orangeLevel}")
                Timber.d("${playerInSetting.player.name} greenLevel ${playerInSetting.player.lettersGuessingLevel.greenLevel}")
                Timber.d("${playerInSetting.player.name} blueLevel ${playerInSetting.player.lettersGuessingLevel.blueLevel}")
                Timber.d("${playerInSetting.player.name} violetLevel ${playerInSetting.player.lettersGuessingLevel.violetLevel}")
                Timber.d("${playerInSetting.player.name} orangeRating ${playerInSetting.player.rating.orangeRating}")
                Timber.d("${playerInSetting.player.name} greenRating ${playerInSetting.player.rating.greenRating}")
                Timber.d("${playerInSetting.player.name} blueRating ${playerInSetting.player.rating.blueRating}")
                Timber.d("${playerInSetting.player.name} violetRating ${playerInSetting.player.rating.violetRating}")
            }
        }
    }
}
