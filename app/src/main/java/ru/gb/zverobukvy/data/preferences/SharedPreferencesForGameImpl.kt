package ru.gb.zverobukvy.data.preferences

import android.content.Context
import android.content.SharedPreferences
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesForGameImpl @Inject constructor(context: Context) : SharedPreferencesForGame {
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

    companion object {
        private const val KEY_TYPES_CARDS = "KeyTypesCards"
        private const val KEY_NAMES_PLAYERS = "KeyNamesPlayers"
        private const val KEY_FIRST_LAUNCH = "KeyFirstLaunch"
        private const val NAME_SHARED_PREFERENCES = "animal_letters_pref"
    }
}