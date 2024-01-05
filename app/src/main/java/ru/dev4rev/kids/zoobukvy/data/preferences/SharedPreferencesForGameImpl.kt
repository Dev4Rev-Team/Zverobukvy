package ru.dev4rev.kids.zoobukvy.data.preferences

import android.content.Context
import android.content.SharedPreferences
import timber.log.Timber
import javax.inject.Inject

class SharedPreferencesForGameImpl @Inject constructor(context: Context) :
    SharedPreferencesForGame {
    private val sharedPreferencesForGame: SharedPreferences = context.getSharedPreferences(
        NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE
    )

    override fun readTypesCardsSelectedForGame(): List<TypeCardsInSharedPreferences> {
        Timber.d("readTypesCardsSelectedForGame")
        return (sharedPreferencesForGame.getStringSet(KEY_TYPES_CARDS, null)
            ?: setOf()).toList().map {
            TypeCardsInSharedPreferences(it)
        }
    }

    override fun saveTypesCardsSelectedForGame(typesCardsSelectedForGame: List<TypeCardsInSharedPreferences>) {
        Timber.d("saveTypesCardsSelectedForGame")
        sharedPreferencesForGame.edit()
            .putStringSet(KEY_TYPES_CARDS, typesCardsSelectedForGame.map {
                it.nameTypeCard
            }.toSet())
            .apply()
    }

    override fun readNamesPlayersSelectedForGame(): List<String> {
        Timber.d("readNamesPlayersSelectedForGame")
        return (sharedPreferencesForGame.getStringSet(KEY_NAMES_PLAYERS, null)
            ?: setOf()).toList()
    }

    override fun saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame: List<String>) {
        Timber.d("saveNamesPlayersSelectedForGame")
        sharedPreferencesForGame.edit()
            .putStringSet(KEY_NAMES_PLAYERS, namesPlayersSelectedForGame.toSet())
            .apply()
    }

    override fun isFirstLaunch(): Boolean {
        Timber.d("isFirstLaunch")
        val isFirstLaunch = sharedPreferencesForGame.getBoolean(KEY_FIRST_LAUNCH, true)
        if (isFirstLaunch)
            sharedPreferencesForGame.edit()
                .putBoolean(KEY_FIRST_LAUNCH, false)
                .apply()
        return isFirstLaunch
    }

    override fun readSoundStatus(): String? {
        Timber.d("readSoundStatus")
        return sharedPreferencesForGame.getString(KEY_SOUND, null)
    }

    override fun saveSoundStatus(soundStatus: String) {
        Timber.d("saveSoundStatus")
        sharedPreferencesForGame.edit()
            .putString(KEY_SOUND, soundStatus)
            .apply()
    }

    override fun readVoiceActingStatus(): String? {
        Timber.d("readVoiceActingStatus")
        return sharedPreferencesForGame.getString(KEY_VOICE_ACTING, null)
    }

    override fun saveVoiceActingStatus(voiceActingStatus: String) {
        Timber.d("saveVoiceActingStatus")
        sharedPreferencesForGame.edit()
            .putString(KEY_VOICE_ACTING, voiceActingStatus)
            .apply()
    }

    companion object {
        private const val KEY_TYPES_CARDS = "KeyTypesCards"
        private const val KEY_NAMES_PLAYERS = "KeyNamesPlayers"
        private const val KEY_FIRST_LAUNCH = "KeyFirstLaunch"
        private const val KEY_SOUND = "KeySound"
        private const val KEY_VOICE_ACTING = "KeyVoiceActing"
        private const val NAME_SHARED_PREFERENCES = "animal_letters_pref"
    }
}