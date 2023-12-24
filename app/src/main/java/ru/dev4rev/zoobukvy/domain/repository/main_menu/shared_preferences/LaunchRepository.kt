package ru.dev4rev.zoobukvy.domain.repository.main_menu.shared_preferences

interface LaunchRepository {
    fun isFirstLaunch(): Boolean
}