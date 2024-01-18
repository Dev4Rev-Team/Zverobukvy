package ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_players.view_holder

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import ru.dev4rev.kids.zoobukvy.R
import ru.dev4rev.kids.zoobukvy.appComponent
import ru.dev4rev.kids.zoobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.dev4rev.kids.zoobukvy.databinding.FragmentMainMenuItemComputerPlayerModeViewBinding
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.PlayerInSettings

class ComputerPlayerViewHolder(
    private val viewBinding: FragmentMainMenuItemComputerPlayerModeViewBinding,
    private val itemPlayerClickListener: (Int) -> Unit
) :
    BasePlayerViewHolder(viewBinding) {

    private val imageAvatarLoader: ImageAvatarLoader = itemView.context.appComponent.imageAvatarLoader

    override fun bindView(playerInSetting: PlayerInSettings?) {
        playerInSetting?.let {
            viewBinding.run {
                playerNameTextView.text = playerInSetting.player.name
                if (it.isSelectedForGame) {
                    playerCardConstraintView.background = getDrawable(itemView.context, R.drawable.background_user_card)
                    playerStateCardView.setCardBackgroundColor(itemView.context.getColor(R.color.color_green_pastel))
                } else {
                    playerCardConstraintView.background = getDrawable(itemView.context, R.color.transparent)
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
