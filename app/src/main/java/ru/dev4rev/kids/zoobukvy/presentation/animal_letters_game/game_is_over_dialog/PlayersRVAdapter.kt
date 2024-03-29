package ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.game_is_over_dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import ru.dev4rev.kids.zoobukvy.appComponent
import ru.dev4rev.kids.zoobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.dev4rev.kids.zoobukvy.databinding.DialogFragmentGameIsOverItemBinding
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player
import timber.log.Timber

class PlayersRVAdapter(
    private val players: List<PlayerUI>,
    private val playerBefore: List<Player.HumanPlayer>,
    private val playerAfter: List<Player.HumanPlayer>
) :
    RecyclerView.Adapter<PlayersRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DialogFragmentGameIsOverItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return players.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setPlayer(players[position], playerBefore, playerAfter)
    }

    class ViewHolder(private val binding: DialogFragmentGameIsOverItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var imageAvatarLoader: ImageAvatarLoader =
            itemView.context.appComponent.imageAvatarLoader

        fun setPlayer(
            playerUI: PlayerUI,
            playerBefore: List<Player.HumanPlayer>,
            playerAfter: List<Player.HumanPlayer>
        ) {
            val name = playerUI.player.name
            binding.playerTextView.text = playerUI.player.name
            imageAvatarLoader.loadImageAvatar(playerUI.player.avatar, binding.playAvatarImageView)
            binding.scoreTextView.text = playerUI.scoreInCurrentGame.toString()

            if (playerUI.player is Player.ComputerPlayer) {
                return
            }

            val findBefore = playerBefore.find { it.name == name }
            val findAfter = playerAfter.find { it.name == name }

            if (findBefore != null && findAfter != null) {
                val orange = findAfter.rating.orangeRating - findBefore.rating.orangeRating
                val green = findAfter.rating.greenRating - findBefore.rating.greenRating
                val blue = findAfter.rating.blueRating - findBefore.rating.blueRating
                val violet = findAfter.rating.violetRating - findBefore.rating.violetRating

                showCard(orange, binding.orangeRatingCardView, binding.orangeRatingTextView)
                showCard(green, binding.greenRatingCardView, binding.greenRatingTextView)
                showCard(blue, binding.blueRatingCardView, binding.blueRatingTextView)
                showCard(violet, binding.violetRatingCardView, binding.violetRatingTextView)
            } else {
                Timber.tag(this.javaClass.simpleName).d("don't find list after/before")
            }
        }

        private fun showCard(
            rating: Int, ratingCardView: MaterialCardView, ratingTextView: MaterialTextView
        ) {
            if (rating > 0) {
                ratingCardView.visibility = View.VISIBLE
                ratingTextView.text = "+$rating" //todo
            } else {
                ratingCardView.visibility = View.GONE
            }
        }
    }

}