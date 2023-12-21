package ru.gb.zverobukvy.presentation.animal_letters_game.game_is_over_dialog

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl
import ru.gb.zverobukvy.databinding.DialogFragmentGameIsOverItemBinding
import ru.gb.zverobukvy.domain.entity.player.Player

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

        private var imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl

        fun setPlayer(
            playerUI: PlayerUI,
            playerBefore: List<Player.HumanPlayer>,
            playerAfter: List<Player.HumanPlayer>
        ) {
            val name = playerUI.player.name
            binding.playerTextView.text = playerUI.player.name
            imageAvatarLoader.loadImageAvatar(playerUI.player.avatar, binding.playAvatarImageView)
            binding.scoreTextView.text = playerUI.scoreInCurrentGame.toString()
            binding.blueRatingCardView.visibility = View.VISIBLE
            binding.blueRatingTextView.text = "+1"
//            val findBefore = playerBefore.find { it.name == name }
//            val findAfter = playerAfter.find { it.name == name }
//
//            if (findBefore != null && findAfter != null){
//                var blue = findAfter.rating.blueRating - findBefore.rating.blueRating
//                val green = findAfter.rating.greenRating - findBefore.rating.greenRating
//                val orange = findAfter.rating.orangeRating - findBefore.rating.orangeRating
//                val violet = findAfter.rating.violetRating - findBefore.rating.violetRating
//
//                blue = 1
//                if(blue > 0 ){
//                    binding.blueRatingCardView.visibility = View.VISIBLE
//                    binding.blueRatingTextView.text = "+$blue"
//                }else{
//                    binding.blueRatingCardView.visibility = View.INVISIBLE
//                }
//            }else{
//                throw IllegalArgumentException("don't find list after/before")
//            }
        }
    }

}