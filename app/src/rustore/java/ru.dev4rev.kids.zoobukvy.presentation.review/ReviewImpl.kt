package ru.dev4rev.kids.zoobukvy.presentation.review

import android.app.Activity
import ru.dev4rev.kids.zoobukvy.domain.repository.UserFeedbackRepository
import ru.rustore.sdk.core.tasks.OnCompleteListener
import ru.rustore.sdk.review.RuStoreReviewManager
import ru.rustore.sdk.review.RuStoreReviewManagerFactory
import ru.rustore.sdk.review.model.ReviewInfo

class ReviewImpl(private val userFeedback: UserFeedbackRepository) : Review {
    private var reviewManager: RuStoreReviewManager? = null
    private var reviewInfo: ReviewInfo? = null
    private var isFeedback = userFeedback.isFeedback()

    override fun requestReviewFlow(activity: Activity) {
        if (isFeedback) return

        reviewManager = RuStoreReviewManagerFactory.create(activity)
        reviewManager?.apply {
            requestReviewFlow().addOnCompleteListener(object : OnCompleteListener<ReviewInfo> {
                override fun onFailure(throwable: Throwable) {
                }

                override fun onSuccess(result: ReviewInfo) {
                    this@ReviewImpl.reviewInfo = result
                }
            })
        }

    }

    override fun launchReviewFlow(activity: Activity, onCompleteListener: () -> Unit) {
        if (isFeedback || reviewManager == null || reviewInfo == null) {
            onCompleteListener.invoke()
            return
        }
        reviewManager!!.launchReviewFlow(reviewInfo!!)
            .addOnCompleteListener(object : OnCompleteListener<Unit> {
                override fun onFailure(throwable: Throwable) {
                    onCompleteListener.invoke()
                }

                override fun onSuccess(result: Unit) {
                    userFeedback.fixFeedback()
                    onCompleteListener.invoke()
                }
            })
    }
}