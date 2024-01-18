package ru.dev4rev.kids.zoobukvy.data.preferences

interface SharedPreferencesForUserFeedback {

    fun isFeedback(): Boolean

    fun fixFeedback()
}