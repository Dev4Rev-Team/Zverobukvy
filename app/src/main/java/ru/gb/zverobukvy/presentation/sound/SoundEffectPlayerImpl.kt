package ru.gb.zverobukvy.presentation.sound

import android.content.Context
import android.media.MediaPlayer
import javax.inject.Inject

class SoundEffectPlayerImpl @Inject constructor(val context: Context) : SoundEffectPlayer {
    private val soundsMap = mutableMapOf<SoundEnum, MediaPlayer>()

    init {
        SoundEnum.values().forEach {

            soundsMap[it] = MediaPlayer().apply {
                val descriptor = context.assets.openFd(it.assetPath)
                setDataSource(
                    descriptor.fileDescriptor,
                    descriptor.startOffset,
                    descriptor.length
                )
                descriptor.close()

                prepare()
                setVolume(1f, 1f)
                isLooping = false
            }

        }
    }

    override fun play(soundEnum: SoundEnum) {
        soundsMap[soundEnum]?.let {
            if (it.isPlaying) {
                it.pause()
                it.seekTo(0)
            }
            it.start()
        }
    }


}