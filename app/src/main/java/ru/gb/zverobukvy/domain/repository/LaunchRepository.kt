package ru.gb.zverobukvy.domain.repository

interface LaunchRepository {
    fun isFirstLaunch(): Boolean
}