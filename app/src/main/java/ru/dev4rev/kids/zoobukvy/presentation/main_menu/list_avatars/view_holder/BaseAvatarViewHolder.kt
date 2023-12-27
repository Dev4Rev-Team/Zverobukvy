package ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_avatars.view_holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.dev4rev.kids.zoobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Avatar

abstract class BaseAvatarViewHolder(viewBinding: FragmentMainMenuItemAvatarBinding) :
    ViewHolder(viewBinding.root) {
    abstract fun bindView(avatar: Avatar)
}