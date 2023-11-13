package ru.gb.zverobukvy.data.view_rating_provider

import ru.gb.zverobukvy.domain.entity.player.Rating


fun interface ViewRatingProviderFactory {

    fun create(rating: Rating): ViewRatingProvider
}