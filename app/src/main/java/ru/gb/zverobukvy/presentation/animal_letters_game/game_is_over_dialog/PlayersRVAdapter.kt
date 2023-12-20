package ru.gb.zverobukvy.presentation.animal_letters_game.game_is_over_dialog

import android.view.LayoutInflater
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
        holder.setPlayer(players[position])
    }

    class ViewHolder(private val binding: DialogFragmentGameIsOverItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl

        fun setPlayer(playerUI: PlayerUI) {
            binding.playerTextView.text = playerUI.player.name
            imageAvatarLoader.loadImageAvatar(playerUI.player.avatar, binding.playAvatarImageView)
            binding.scoreTextView.text = playerUI.scoreInCurrentGame.toString()
        }
    }

}