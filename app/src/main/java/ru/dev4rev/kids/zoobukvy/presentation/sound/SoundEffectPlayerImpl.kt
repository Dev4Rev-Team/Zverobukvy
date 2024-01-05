package ru.dev4rev.kids.zoobukvy.presentation.sound

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import ru.dev4rev.kids.zoobukvy.configuration.Conf
import ru.dev4rev.kids.zoobukvy.domain.repository.animal_letter_game.AnimalLettersGameRepository
import javax.inject.Inject

class SoundEffectPlayerImpl @Inject constructor(
    private val context: Context,
    private val animalLettersCardsRepository: AnimalLettersGameRepository
) : SoundEffectPlayer {
    private val soundPool: SoundPool
    private val soundsMap = mutableMapOf<String, Int>()
    private val isLoad = mutableSetOf<Int>()
    private val queueSound = mutableSetOf<Int>()
    private val channelIsLoad = Channel<Int>()

    private val myCoroutineScope = CoroutineScope(Dispatchers.Default)
    private val job: Job

    private var enable = true
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager?

    init {
        soundPool = createSoundPool()

        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                isLoad.add(sampleId)
                myCoroutineScope.launch {
                    channelIsLoad.send(sampleId)
                }
                if (queueSound.contains(sampleId)) {
                    myCoroutineScope.launch {
                        playSound(sampleId)
                    }
                    queueSound.remove(sampleId)
                }
            }
        }

        job = myCoroutineScope.launch {
            SoundEnum.values().forEach {
                try {
                    loadSoundMap(it.assetPath, ASSETS_PATH_SOUND_SYSTEM + it.assetPath)
                } catch (e: Exception) {
                    throw IllegalStateException("sound no element systemSound ${it.assetPath}")
                }
            }

            animalLettersCardsRepository.getLetterCards().forEach {
                try {
                    loadSoundMap(it.soundName, ASSETS_PATH_SOUND_LETTERS + it.soundName)
                } catch (e: Exception) {
                    throw IllegalStateException("sound no element LettersCards ${it.soundName}")
                }
            }


            animalLettersCardsRepository.getWordCards().forEach {
                try {
                    loadSoundMap(it.soundName, ASSETS_PATH_SOUND_WORDS + it.soundName)
                } catch (e: Exception) {
                    if (!Conf.DEBUG_DISABLE_CHECK_SOUND_FILE) {
                        throw IllegalStateException("sound no element WordCard ${it.soundName}")
                    }
                }
            }

            myCoroutineScope.launch { channelIsLoad.cancel() }
        }
    }

    private suspend fun loadSoundMap(soundName: String, pathFile: String) {
        if (!soundsMap.contains(soundName)) {
            soundsMap[soundName] = loadSound(pathFile)
            while (channelIsLoad.receive() != soundsMap[soundName]) {
                yield()
            }
        }
    }

    private fun loadSound(assetPath: String): Int {
        val descriptor = context.assets.openFd(assetPath)
        val idStream = soundPool.load(descriptor, DEFAULT_PRIORITY_LOAD)
        descriptor.close()
        return idStream
    }

    @SuppressLint("ObsoleteSdkInt")
    fun createSoundPool(): SoundPool {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool()
        } else {
            createOldSoundPool()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createNewSoundPool(): SoundPool {
        val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
        return SoundPool.Builder().setMaxStreams(MAX_STREAM).setAudioAttributes(attributes).build()
    }

    @Suppress("deprecation")
    private fun createOldSoundPool(): SoundPool {
        return SoundPool(MAX_STREAM, AudioManager.STREAM_MUSIC, 0)
    }

    override fun play(soundEnum: SoundEnum) {
        myCoroutineScope.launch {
            var idStream = soundsMap[soundEnum.assetPath]
            if (idStream == null) {
                soundsMap[soundEnum.assetPath] =
                    loadSound(ASSETS_PATH_SOUND_SYSTEM + soundEnum.assetPath)
                idStream = soundsMap[soundEnum.assetPath]
            }
            playSound(idStream)
        }
    }

    override fun play(key: String) {
        if (!enable) return
        myCoroutineScope.launch {
            var idStream = soundsMap[key]
            if (idStream == null) {
                val path = if (animalLettersCardsRepository.getLetterCards()
                        .find { it.soundName == key } != null) {
                    ASSETS_PATH_SOUND_LETTERS + key
                } else {
                    ASSETS_PATH_SOUND_WORDS + key
                }
                soundsMap[key] = loadSound(path)
                idStream = soundsMap[key]
            }
            playSound(idStream)
        }
    }

    override fun setEnable(enable: Boolean) {
        this.enable = enable
    }

    private fun playSound(idStream: Int?) {
        if (isLoad.contains(idStream)) {
            audioManager?.let {
                val curVolume = it.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
                val maxVolume = it.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
                val leftVolume = curVolume / maxVolume
                val rightVolume = curVolume / maxVolume
                val priority = 1
                val noLoop = 0
                val normalPlaybackRate = 1f
                soundPool.play(
                    idStream!!,
                    leftVolume,
                    rightVolume,
                    priority,
                    noLoop,
                    normalPlaybackRate
                )
            }
        } else {
            idStream?.let { queueSound.add(it) }
        }
    }

    companion object {
        const val MAX_STREAM = 3
        const val DEFAULT_PRIORITY_LOAD = 1
        const val ASSETS_PATH_SOUND_SYSTEM = "sounds/system/"
        const val ASSETS_PATH_SOUND_WORDS = "sounds/words/"
        const val ASSETS_PATH_SOUND_LETTERS = "sounds/letters/"
    }

}