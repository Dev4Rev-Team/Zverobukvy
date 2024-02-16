package ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_players.view_holder

import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import ru.dev4rev.kids.zoobukvy.R
import ru.dev4rev.kids.zoobukvy.appComponent
import ru.dev4rev.kids.zoobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.dev4rev.kids.zoobukvy.data.view_rating_provider.Decoration
import ru.dev4rev.kids.zoobukvy.data.view_rating_provider.Rank
import ru.dev4rev.kids.zoobukvy.data.view_rating_provider.ViewRating
import ru.dev4rev.kids.zoobukvy.data.view_rating_provider.ViewRatingProvider
import ru.dev4rev.kids.zoobukvy.databinding.FragmentMainMenuItemPlayerModeViewBinding
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.PlayerInSettings
import timber.log.Timber

class PlayerViewHolder(
    private val viewBinding: FragmentMainMenuItemPlayerModeViewBinding,
    private val itemPlayerClickListener: (Int) -> Unit,
    private val editMenuClickListener: (Int) -> Unit,
) :
    BasePlayerViewHolder(viewBinding) {

    private val imageAvatarLoader: ImageAvatarLoader = itemView.context.appComponent.imageAvatarLoader

    private lateinit var viewRatingProvider: ViewRatingProvider

    override fun bindView(playerInSetting: PlayerInSettings?) {
        playerInSetting?.let {
            viewRatingProvider = itemView.context.appComponent.getViewRatingProviderFactory().create(it.player.rating)
            initViewRank()
            initViewRating()
            viewBinding.run {
                playerNameTextView.text = it.player.name
                if (it.isSelectedForGame) {
                    playerCardConstraintLayout.background = getDrawable(itemView.context, R.drawable.background_user_card)
                    playerStateCardView.setCardBackgroundColor(itemView.context.getColor(R.color.color_green_pastel))
                } else {
                    playerCardConstraintLayout.background = getDrawable(itemView.context, R.color.transparent)
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

    private fun initViewRank() {
        viewBinding.run {
            viewRatingProvider.getRank().let {
                if (it == Rank.DEFAULT)
                    rankTextView.visibility = View.GONE
                else
                    rankTextView.visibility = View.VISIBLE
                rankTextView.text = itemView.context.getString(it.rankNameId)
                rankTextView.setTextColor(itemView.context.getColor(it.rankTextColorId))
                avatar.strokeColor = itemView.context.getColor(it.borderRankColorId)
            }
        }
    }

    private fun initViewRating() {
        viewBinding.run {
            initRatingCardView(
                viewRatingProvider.getOrangeRating(),
                orangeRatingCardView,
                orangeRatingTextView,
                orangeDiamondImageView
            )
            initRatingCardView(
                viewRatingProvider.getGreenRating(),
                greenRatingCardView,
                greenRatingTextView,
                greenDiamondImageView
            )
            initRatingCardView(
                viewRatingProvider.getBlueRating(),
                blueRatingCardView,
                blueRatingTextView,
                blueDiamondImageView
            )
            initRatingCardView(
                viewRatingProvider.getVioletRating(),
                violetRatingCardView,
                violetRatingTextView,
                violetDiamondImageView
            )
        }
    }

    private fun initRatingCardView(
        viewRating: ViewRating,
        ratingCardView: MaterialCardView,
        ratingTextView: MaterialTextView,
        diamondImageView: AppCompatImageView
    ) {
        viewRating.let {
            if (it.rating != 0) {
                ratingCardView.visibility = View.VISIBLE
                ratingCardView.strokeColor =
                    itemView.context.getColor(it.decoration.colorId)
                if (it.decoration == Decoration.DIAMOND) {
                    diamondImageView.visibility = View.VISIBLE
                    ratingTextView.visibility = View.GONE
                } else {
                    diamondImageView.visibility = View.GONE
                    ratingTextView.visibility = View.VISIBLE
                    ratingTextView.text = it.rating.toString()
                }
            } else
                ratingCardView.visibility = View.GONE
        }
    }
}
