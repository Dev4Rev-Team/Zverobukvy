package ru.dev4rev.kids.zoobukvy.domain.repository

interface UserFeedbackRepository {
    fun isFeedback(): Boolean

    fun fixFeedback()
}