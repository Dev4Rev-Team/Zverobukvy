package ru.gb.zverobukvy.presentation.animal_letters_game.dialog.game_is_over_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.gb.zverobukvy.databinding.DialogFragmentGameIsOverItemBinding
import ru.gb.zverobukvy.utility.ui.ExtractAvatarDrawableHelper

class PlayersRVAdapter(private val players: List<PlayerUI>) :
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
        fun setPlayer(playerUI: PlayerUI) {
            binding.playerTextView.text = playerUI.player.name
            binding.playAvatarImageView.load(
                ExtractAvatarDrawableHelper.extractDrawable(
                    itemView.context,
                    playerUI.player.avatar
                )
            )
            binding.scoreTextView.text = playerUI.scoreInCurrentGame.toString()
        }
    }

}