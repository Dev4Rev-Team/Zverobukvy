package ru.gb.zverobukvy.presentation.main_menu.view.list_players.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemPlayerModeAddBinding
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemPlayerModeEditBinding
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemPlayerModeViewBinding
import ru.gb.zverobukvy.presentation.main_menu.view.list_players.click_listener_owner.AddPlayerClickListenerOwner
import ru.gb.zverobukvy.presentation.main_menu.view.list_players.view_holder.AddPlayerViewHolder
import ru.gb.zverobukvy.presentation.main_menu.view.list_players.view_holder.BaseViewHolder
import ru.gb.zverobukvy.presentation.main_menu.view.list_players.click_listener_owner.EditPlayerClickListenerOwner
import ru.gb.zverobukvy.presentation.main_menu.view.list_players.view_holder.EditPlayerViewHolder
import ru.gb.zverobukvy.presentation.main_menu.view.list_players.click_listener_owner.PlayerClickListenerOwner
import ru.gb.zverobukvy.presentation.main_menu.view.list_players.view_holder.PlayerViewHolder

class PlayersAdapter(
    private val playerClickListenerOwner: PlayerClickListenerOwner,
    private val editPlayerClickListenerOwner: EditPlayerClickListenerOwner,
    private val addPlayerClickListenerOwner: AddPlayerClickListenerOwner
) :
    BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            NOT_EDIT_ITEM_TYPE -> PlayerViewHolder(
                FragmentMainMenuItemPlayerModeViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                playerClickListenerOwner.itemPlayerClickListener,
                playerClickListenerOwner.editMenuClickListener
            )

            EDIT_ITEM_TYPE -> EditPlayerViewHolder(
                FragmentMainMenuItemPlayerModeEditBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                editPlayerClickListenerOwner.saveChangedPlayerClickListener,
                editPlayerClickListenerOwner.cancelChangedPlayerClickListener,
                editPlayerClickListenerOwner.queryRemovePlayersClickListener
            )

            ADD_ITEM_TYPE -> AddPlayerViewHolder(
                FragmentMainMenuItemPlayerModeAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                addPlayerClickListenerOwner.addPlayerClickListener
            )

            else -> AddPlayerViewHolder(
                FragmentMainMenuItemPlayerModeAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                addPlayerClickListenerOwner.addPlayerClickListener
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) =
        holder.bindView(players[position])

    override fun getItemCount(): Int = players.size

    override fun getItemViewType(position: Int): Int {
        players[position].let {
            if (it == null)
                return ADD_ITEM_TYPE
            when (it.inEditingState) {
                false -> return NOT_EDIT_ITEM_TYPE
                true -> return EDIT_ITEM_TYPE
            }
        }
    }

    companion object {
        const val NOT_EDIT_ITEM_TYPE = 1
        const val EDIT_ITEM_TYPE = 2
        const val ADD_ITEM_TYPE = 3

    }
}