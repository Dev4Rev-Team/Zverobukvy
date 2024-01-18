package ru.dev4rev.kids.zoobukvy.presentation.review

import android.app.Activity
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import ru.dev4rev.kids.zoobukvy.configuration.Conf
import ru.dev4rev.kids.zoobukvy.data.preferences.SharedPreferencesForUserFeedback
import ru.dev4rev.kids.zoobukvy.domain.repository.UserFeedbackRepository

class ReviewImpl(private val userFeedback: UserFeedbackRepository) : Review {

    private var reviewManager: ReviewManager? = null
    private var reviewInfo: ReviewInfo? = null
    private var isFeedback = userFeedback.isFeedback()

    override fun requestReviewFlow(activity: Activity) {
        if (isFeedback) return

        reviewManager =
            if (Conf.DEBUG) FakeReviewManager(activity) else ReviewManagerFactory.create(activity)
        reviewManager?.apply {
            requestReviewFlow().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reviewInfo = task.result
                }
            }
        }
    }

    override fun launchReviewFlow(activity: Activity, onCompleteListener: () -> Unit) {
        if (isFeedback || reviewManager == null || reviewInfo == null) {
            onCompleteListener.invoke()
            return
        }
        val flow = reviewManager!!.launchReviewFlow(activity, reviewInfo!!)
        flow.addOnCompleteListener { _ ->
            userFeedback.fixFeedback()
            onCompleteListener.invoke()
        }
    }
}
