package ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_players.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.PlayerInSettings
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_players.view_holder.BasePlayerViewHolder

abstract class BaseAdapter: RecyclerView.Adapter<BasePlayerViewHolder>() {
    protected var players: List<PlayerInSettings?> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setNewPlayers(newPlayers: List<PlayerInSettings?>){
        players = newPlayers
        notifyDataSetChanged()
    }

    fun changedPlayer(newPlayers: List<PlayerInSettings?>, positionChangedPlayer: Int){
        players = newPlayers
        notifyItemChanged(positionChangedPlayer)
    }

    fun addPlayer(newPlayers: List<PlayerInSettings?>, positionAddPlayer: Int){
        players = newPlayers
        notifyItemInserted(positionAddPlayer)

    }

   fun removePlayer(newPlayers: List<PlayerInSettings?>, positionRemovePlayer: Int){
        players = newPlayers
        notifyItemRemoved(positionRemovePlayer)
    }
}