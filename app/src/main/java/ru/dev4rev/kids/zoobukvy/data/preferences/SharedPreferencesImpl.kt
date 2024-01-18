package ru.dev4rev.kids.zoobukvy.data.preferences

import android.content.Context
import android.content.SharedPreferences
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesImpl @Inject constructor(context: Context) :
    SharedPreferencesForGame, SharedPreferencesForUserFeedback {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE
    )

    override fun readTypesCardsSelectedForGame(): List<TypeCardsInSharedPreferences> {
        Timber.d("readTypesCardsSelectedForGame")
        return (sharedPreferences.getStringSet(KEY_TYPES_CARDS, null)
            ?: setOf()).toList().map {
            TypeCardsInSharedPreferences(it)
        }
    }

    override fun saveTypesCardsSelectedForGame(typesCardsSelectedForGame: List<TypeCardsInSharedPreferences>) {
        Timber.d("saveTypesCardsSelectedForGame")
        sharedPreferences.edit()
            .putStringSet(KEY_TYPES_CARDS, typesCardsSelectedForGame.map {
                it.nameTypeCard
            }.toSet())
            .apply()
    }

    override fun readNamesPlayersSelectedForGame(): List<String> {
        Timber.d("readNamesPlayersSelectedForGame")
        return (sharedPreferences.getStringSet(KEY_NAMES_PLAYERS, null)
            ?: setOf()).toList()
    }

    override fun saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame: List<String>) {
        Timber.d("saveNamesPlayersSelectedForGame")
        sharedPreferences.edit()
            .putStringSet(KEY_NAMES_PLAYERS, namesPlayersSelectedForGame.toSet())
            .apply()
    }

    override fun isFirstLaunch(): Boolean {
        Timber.d("isFirstLaunch")
        val isFirstLaunch = sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true)
        if (isFirstLaunch)
            sharedPreferences.edit()
                .putBoolean(KEY_FIRST_LAUNCH, false)
                .apply()
        return isFirstLaunch
    }

    override fun readSoundStatus(): Boolean {
        Timber.d("readSoundStatus")
        return sharedPreferences.getBoolean(KEY_SOUND, true)
    }

    override fun saveSoundStatus(soundStatus: Boolean) {
        Timber.d("saveSoundStatus")
        sharedPreferences.edit()
            .putBoolean(KEY_SOUND, soundStatus)
            .apply()
    }

    override fun readVoiceActingStatus(): String? {
        Timber.d("readVoiceActingStatus")
        return sharedPreferences.getString(KEY_VOICE_ACTING, null)
    }

    override fun saveVoiceActingStatus(voiceActingStatus: String) {
        Timber.d("saveVoiceActingStatus")
        sharedPreferences.edit()
            .putString(KEY_VOICE_ACTING, voiceActingStatus)
            .apply()
    }

    override fun isFeedback(): Boolean {
        Timber.d("isFeedback")
        return sharedPreferences.getBoolean(KEY_FEEDBACK, false)
    }

    override fun fixFeedback() {
        Timber.d("fixFeedback")
        sharedPreferences.edit()
            .putBoolean(KEY_FEEDBACK, true)
            .apply()
    }

    companion object {
        private const val KEY_TYPES_CARDS = "KeyTypesCards"
        private const val KEY_NAMES_PLAYERS = "KeyNamesPlayers"
        private const val KEY_FIRST_LAUNCH = "KeyFirstLaunch"
        private const val KEY_SOUND = "KeySound"
        private const val KEY_VOICE_ACTING = "KeyVoiceActing"
        private const val KEY_FEEDBACK = "KeyFeedback"
        private const val NAME_SHARED_PREFERENCES = "animal_letters_pref"
    }
}