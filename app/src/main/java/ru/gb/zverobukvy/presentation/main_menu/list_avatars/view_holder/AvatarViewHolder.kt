package ru.gb.zverobukvy.presentation.main_menu.list_avatars.view_holder

import coil.decode.SvgDecoder
import coil.load
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemAvatarBinding
import ru.gb.zverobukvy.domain.entity.Avatar
import ru.gb.zverobukvy.utility.ui.ExtractAvatarDrawableHelper
import java.nio.ByteBuffer

class AvatarViewHolder(
    private val viewBinding: FragmentMainMenuItemAvatarBinding,
    private val avatarClickListener: (Int) -> Unit,
) : BaseAvatarViewHolder(viewBinding) {
    override fun bindView(avatar: Avatar) {
        if (avatar.isStandard) {
            viewBinding.playerAvatarImageView.load(
                ExtractAvatarDrawableHelper.extractDrawable(itemView.context, avatar)
            )
        } else {
            viewBinding.playerAvatarImageView.load(ByteBuffer.wrap(avatar.imageName.toByteArray())) {
                decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
            }
        }
        viewBinding.avatarCardView.setOnClickListener {
            avatarClickListener(adapterPosition)
        }
    }
}