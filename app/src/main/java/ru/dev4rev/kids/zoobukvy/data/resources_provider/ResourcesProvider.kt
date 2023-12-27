package ru.dev4rev.kids.zoobukvy.data.resources_provider

interface ResourcesProvider {
    fun getString(string: StringEnum): String
}