package ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_avatars

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.dev4rev.kids.zoobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Avatar
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_avatars.view_holder.AddAvatarViewHolder
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_avatars.view_holder.AvatarViewHolder
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_avatars.view_holder.BaseAvatarViewHolder

class AvatarsAdapter(
    private val avatarClickListener: (Int) -> Unit,
    private val addAvatarsClickListener: () -> Unit
) : RecyclerView.Adapter<BaseAvatarViewHolder>() {

    private var avatars = listOf<Avatar>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == ADD_ITEM_TYPE)
            AddAvatarViewHolder(
                FragmentMainMenuItemAvatarBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), addAvatarsClickListener
            )
        else
            AvatarViewHolder(
                FragmentMainMenuItemAvatarBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), avatarClickListener
            )

    override fun getItemCount(): Int =
        avatars.size

    override fun onBindViewHolder(holder: BaseAvatarViewHolder, position: Int) {
        holder.bindView(avatars[position])
    }

    override fun getItemViewType(position: Int): Int =
        if (avatars[position].imageName == Avatar.ADD_IMAGE_NAME)
            ADD_ITEM_TYPE
        else
            ITEM_TYPE

    @SuppressLint("NotifyDataSetChanged")
    fun setAvatars(avatars: List<Avatar>) {
        val size = this.avatars.size
        this.avatars = avatars
        if (size != avatars.size)
            notifyDataSetChanged()
        else
            notifyItemChanged(size - 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetAvatars(){
        avatars = listOf()
        notifyDataSetChanged()
    }

    companion object {
        const val ITEM_TYPE = 1
        const val ADD_ITEM_TYPE = 2
    }

}