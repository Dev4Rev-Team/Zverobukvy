package ru.gb.zverobukvy.presentation.main_menu.view.list_players.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import ru.gb.zverobukvy.domain.entity.PlayerInSettings
import ru.gb.zverobukvy.presentation.main_menu.view.list_players.view_holder.BaseViewHolder

abstract class BaseAdapter: RecyclerView.Adapter<BaseViewHolder>() {
    protected var players: List<PlayerInSettings?> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setPlayers(newPlayers: List<PlayerInSettings?>){
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