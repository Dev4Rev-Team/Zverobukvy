package ru.gb.zverobukvy.presentation.main_menu.list_players.view_holder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.appComponent
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl
import ru.gb.zverobukvy.data.view_rating_provider.Decoration
import ru.gb.zverobukvy.data.view_rating_provider.Rank
import ru.gb.zverobukvy.data.view_rating_provider.ViewRating
import ru.gb.zverobukvy.data.view_rating_provider.ViewRatingProvider
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemPlayerModeViewBinding
import ru.gb.zverobukvy.presentation.main_menu.PlayerInSettings
import timber.log.Timber

/*@AssistedFactory
interface PlayerViewHolderFactory {
    fun create(
        @Assisted viewBinding: FragmentMainMenuItemPlayerModeViewBinding,
        @Assisted("itemPlayerClickListener") itemPlayerClickListener: (Int) -> Unit,
        @Assisted("editMenuClickListener") editMenuClickListener: (Int) -> Unit,
    ): PlayerViewHolder
}

class PlayerViewHolder @AssistedInject constructor(
    @Assisted private val viewBinding: FragmentMainMenuItemPlayerModeViewBinding,
    @Assisted("itemPlayerClickListener") private val itemPlayerClickListener: (Int) -> Unit,
    @Assisted("editMenuClickListener") private val editMenuClickListener: (Int) -> Unit,
    private val viewRatingProviderFactory: ViewRatingProviderFactory,
)*/

class PlayerViewHolder(
    private val viewBinding: FragmentMainMenuItemPlayerModeViewBinding,
    private val itemPlayerClickListener: (Int) -> Unit,
    private val editMenuClickListener: (Int) -> Unit,
) :
    BasePlayerViewHolder(viewBinding) {

    private val imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl

    private lateinit var viewRatingProvider: ViewRatingProvider

    override fun bindView(playerInSetting: PlayerInSettings?) {
        playerInSetting?.let {

            val factory = itemView.context.appComponent.getViewRatingProviderFactory()

            viewRatingProvider = factory.create(it.player.rating)
            initViewRank()
            initViewRating()
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

    private fun initViewRank() {
        viewBinding.run {
            when (viewRatingProvider.getRank()) {
                Rank.LEARNER -> {
                    rankTextView.text = itemView.context.getString(R.string.learner)
                    rankTextView.setTextColor(itemView.context.getColor(R.color.rank_learner))
                    avatar.strokeColor = itemView.context.getColor(R.color.border_rank_learner)
                }

                Rank.EXPERT -> {
                    rankTextView.text = itemView.context.getString(R.string.expert)
                    rankTextView.setTextColor(itemView.context.getColor(R.color.rank_expert))
                    avatar.strokeColor = itemView.context.getColor(R.color.border_rank_expert)
                }

                Rank.MASTER -> {
                    rankTextView.text = itemView.context.getString(R.string.master)
                    rankTextView.setTextColor(itemView.context.getColor(R.color.rank_master))
                    avatar.strokeColor = itemView.context.getColor(R.color.border_rank_master)
                }

                Rank.GENIUS -> {
                    rankTextView.text = itemView.context.getString(R.string.genius)
                    rankTextView.setTextColor(itemView.context.getColor(R.color.rank_genius))
                    avatar.strokeColor = itemView.context.getColor(R.color.border_rank_genius)
                }

                Rank.HERO -> {
                    rankTextView.text = itemView.context.getString(R.string.hero)
                    rankTextView.setTextColor(itemView.context.getColor(R.color.rank_hero))
                    avatar.strokeColor = itemView.context.getColor(R.color.border_rank_hero)
                }

                Rank.LEGEND -> {
                    rankTextView.text = itemView.context.getString(R.string.legend)
                    rankTextView.setTextColor(itemView.context.getColor(R.color.rank_legend))
                    avatar.strokeColor = itemView.context.getColor(R.color.border_rank_legend)
                }

                Rank.DEFAULT -> rankTextView.visibility = View.GONE
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
            if (it.rating != 0 || it.decoration != Decoration.DEFAULT) {
                ratingCardView.visibility = View.VISIBLE
                ratingCardView.strokeColor =
                    itemView.context.getColor(it.decoration.color)
                if (it.decoration == Decoration.DIAMOND)
                    diamondImageView.visibility = View.VISIBLE
                else if (it.rating != 0)
                    ratingTextView.text = it.rating.toString()
            }
        }
    }
}
