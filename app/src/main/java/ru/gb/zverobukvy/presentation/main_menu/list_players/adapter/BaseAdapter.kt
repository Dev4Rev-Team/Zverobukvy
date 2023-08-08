package ru.gb.zverobukvy.presentation.main_menu.list_players.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.gb.zverobukvy.domain.entity.PlayerInSettings
import ru.gb.zverobukvy.presentation.main_menu.list_players.view_holder.BaseViewHolder

abstract class BaseAdapter: RecyclerView.Adapter<BaseViewHolder>() {
    protected var players: List<PlayerInSettings?> = listOf()

    protected fun changedPlayer(newPlayers: List<PlayerInSettings?>, positionChangedPlayer: Int){
        players = newPlayers
        notifyItemChanged(positionChangedPlayer)
    }

    protected fun addPlayer(newPlayers: List<PlayerInSettings?>, positionAddPlayer: Int){
        players = newPlayers
        notifyItemInserted(positionAddPlayer)
    }

    protected fun removePlayer(newPlayers: List<PlayerInSettings?>, positionRemovePlayer: Int){
        players = newPlayers
        notifyItemRemoved(positionRemovePlayer)
    }
}