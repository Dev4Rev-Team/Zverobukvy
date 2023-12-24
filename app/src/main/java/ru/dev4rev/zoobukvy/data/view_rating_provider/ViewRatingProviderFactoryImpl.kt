package ru.dev4rev.zoobukvy.data.view_rating_provider

import ru.dev4rev.zoobukvy.domain.entity.player.Rating
import javax.inject.Inject

class ViewRatingProviderFactoryImpl @Inject constructor() : ViewRatingProviderFactory {
    override fun create(rating: Rating): ViewRatingProvider {
        return ViewRatingProviderImpl(rating)
    }
}