package ru.dev4rev.kids.zoobukvy.data.view_rating_provider

import ru.dev4rev.kids.zoobukvy.domain.entity.player.Rating

fun interface ViewRatingProviderFactory {

    fun create(rating: Rating): ViewRatingProvider
}