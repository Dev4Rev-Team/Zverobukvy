package ru.dev4rev.kids.zoobukvy.domain.repository.main_menu.shared_preferences

interface LaunchRepository {
    fun isFirstLaunch(): Boolean
}