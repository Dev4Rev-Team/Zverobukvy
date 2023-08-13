package ru.gb.zverobukvy.presentation.game_zverobukvy.game_is_over_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gb.zverobukvy.databinding.DialogFragmentGameIsOverItemBinding

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
            binding.playerTextView.text = playerUI.name
            binding.scoreTextView.text = playerUI.scoreInCurrentGame.toString()
        }

    }

}