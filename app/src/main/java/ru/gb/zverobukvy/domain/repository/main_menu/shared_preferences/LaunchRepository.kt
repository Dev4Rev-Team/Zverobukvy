package ru.gb.zverobukvy.domain.repository.main_menu.shared_preferences

interface LaunchRepository {
    fun isFirstLaunch(): Boolean
}