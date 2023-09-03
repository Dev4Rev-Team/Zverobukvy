package ru.gb.zverobukvy.presentation.main_menu.list_avatars.view_holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.gb.zverobukvy.domain.entity.Avatar

abstract class BaseAvatarViewHolder(viewBinding: FragmentMainMenuItemAvatarBinding) :
    ViewHolder(viewBinding.root) {
    abstract fun bindView(avatar: Avatar)
}