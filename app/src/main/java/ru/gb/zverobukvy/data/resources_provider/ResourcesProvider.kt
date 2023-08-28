package ru.gb.zverobukvy.data.resources_provider

interface ResourcesProvider {
    fun getString(string: StringEnum): String
}