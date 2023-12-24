package ru.dev4rev.zoobukvy.data.resources_provider

interface ResourcesProvider {
    fun getString(string: StringEnum): String
}