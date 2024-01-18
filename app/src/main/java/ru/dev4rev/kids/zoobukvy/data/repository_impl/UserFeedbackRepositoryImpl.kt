package ru.dev4rev.kids.zoobukvy.data.repository_impl

import ru.dev4rev.kids.zoobukvy.data.preferences.SharedPreferencesForUserFeedback
import ru.dev4rev.kids.zoobukvy.domain.repository.UserFeedbackRepository
import javax.inject.Inject

class UserFeedbackRepositoryImpl @Inject constructor(private val sharedPreferencesForUserFeedback: SharedPreferencesForUserFeedback): UserFeedbackRepository {
    override fun isFeedback(): Boolean = sharedPreferencesForUserFeedback.isFeedback()

    override fun fixFeedback() {
        sharedPreferencesForUserFeedback.fixFeedback()
    }
}