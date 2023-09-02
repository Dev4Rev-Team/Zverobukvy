package ru.gb.zverobukvy.presentation.main_menu.list_avatars

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.gb.zverobukvy.domain.entity.Avatar

class AvatarsAdapter(
    private val avatarClickListener: (Int) -> Unit,
) : RecyclerView.Adapter<AvatarViewHolder>() {

    private var avatars = listOf<Avatar>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AvatarViewHolder(
            FragmentMainMenuItemAvatarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), avatarClickListener
        )

    override fun getItemCount(): Int =
        avatars.size

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        holder.binding(avatars[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAvatars(avatars: List<Avatar>){
        this.avatars = avatars
        notifyDataSetChanged()
    }

}