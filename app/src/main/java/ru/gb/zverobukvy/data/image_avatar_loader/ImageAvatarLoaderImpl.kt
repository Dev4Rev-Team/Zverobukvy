package ru.gb.zverobukvy.data.image_avatar_loader

import android.widget.ImageView
import coil.decode.SvgDecoder
import coil.load
import ru.gb.zverobukvy.domain.entity.player.Avatar
import java.nio.ByteBuffer

object ImageAvatarLoaderImpl: ImageAvatarLoader {
    override fun loadImageAvatar(avatar: Avatar, container: ImageView) {
        if (avatar.isStandard)
            loadDrawable(avatar.imageName, container)
        else
            loadSvg(avatar.imageName, container)
    }

    private fun loadDrawable(imageName: String, container: ImageView){
        container.load(ExtractAvatarDrawableHelper.extractDrawable(container.context, imageName))
    }

    private fun loadSvg(imageName: String, container: ImageView){
        container.load(ByteBuffer.wrap(imageName.toByteArray())){
            decoderFactory { result, options, _ ->
                SvgDecoder(
                    result.source,
                    options
                )
            }
        }
    }
}