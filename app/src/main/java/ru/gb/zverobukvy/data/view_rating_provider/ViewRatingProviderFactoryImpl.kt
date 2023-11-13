package ru.gb.zverobukvy.data.view_rating_provider

import ru.gb.zverobukvy.domain.entity.Rating
import javax.inject.Inject

class ViewRatingProviderFactoryImpl @Inject constructor(): ViewRatingProviderFactory {
    override fun create(rating: Rating): ViewRatingProvider {
        return ViewRatingProviderImpl(rating)
    }
}