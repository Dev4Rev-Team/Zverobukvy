package ru.gb.zverobukvy.presentation.main_menu.list_avatars

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.gb.zverobukvy.domain.entity.Avatar
import ru.gb.zverobukvy.presentation.main_menu.list_avatars.view_holder.AddAvatarViewHolder
import ru.gb.zverobukvy.presentation.main_menu.list_avatars.view_holder.AvatarViewHolder
import ru.gb.zverobukvy.presentation.main_menu.list_avatars.view_holder.BaseAvatarViewHolder

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
        this.avatars = avatars
        notifyDataSetChanged()
    }

    companion object {
        const val ITEM_TYPE = 1
        const val ADD_ITEM_TYPE = 2
    }

}