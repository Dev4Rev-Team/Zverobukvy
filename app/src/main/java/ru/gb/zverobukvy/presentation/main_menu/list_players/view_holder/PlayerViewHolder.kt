package ru.gb.zverobukvy.presentation.main_menu.list_players.view_holder

import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemPlayerModeViewBinding
import ru.gb.zverobukvy.presentation.main_menu.PlayerInSettings
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl
import ru.gb.zverobukvy.data.view_rating_provider.Rank
import ru.gb.zverobukvy.data.view_rating_provider.ViewRatingProvider
import ru.gb.zverobukvy.data.view_rating_provider.ViewRatingProviderImpl
import timber.log.Timber

class PlayerViewHolder(
    private val viewBinding: FragmentMainMenuItemPlayerModeViewBinding,
    private val itemPlayerClickListener: (Int) -> Unit,
    private val editMenuClickListener: (Int) -> Unit
) :
    BasePlayerViewHolder(viewBinding) {

    private val imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl

    private lateinit var viewRatingProvider: ViewRatingProvider

    override fun bindView(playerInSetting: PlayerInSettings?) {
        playerInSetting?.let {
            viewRatingProvider = ViewRatingProviderImpl(it.player.rating)
            viewRating()
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
            }
        }
    }

    private fun viewRating(){
        viewBinding.run {
            rankTextView.text = when(viewRatingProvider.getRank()){
                Rank.LEARNER -> itemView.context.getString(R.string.learner)
                Rank.EXPERT -> itemView.context.getString(R.string.expert)
                Rank.MASTER -> itemView.context.getString(R.string.master)
                Rank.GENIUS -> itemView.context.getString(R.string.genius)
                Rank.HERO -> itemView.context.getString(R.string.hero)
                Rank.LEGEND -> itemView.context.getString(R.string.legend)
            }
            ratingOrangeTextView.text = viewRatingProvider.getOrangeRating().rating.toString()
            ratingGreenTextView.text = viewRatingProvider.getGreenRating().rating.toString()
            ratingBlueTextView.text = viewRatingProvider.getBlueRating().rating.toString()
            ratingVioletTextView.text = viewRatingProvider.getVioletRating().rating.toString()
            //TODO view decoration
        }
    }
}
