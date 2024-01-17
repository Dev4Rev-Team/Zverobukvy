package ru.dev4rev.kids.zoobukvy.presentation.review

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import ru.dev4rev.kids.zoobukvy.configuration.Conf

class ReviewImpl : Review {
    override fun request(activity: Activity, onCompleteListener: () -> Unit) {
        val manager = if (Conf.DEBUG) FakeReviewManager(activity) else ReviewManagerFactory.create(activity)
        val request= manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener { _ ->
//todo
//                    Setting.isGameRating = true
//                    Setting.save()
                    onCompleteListener()
                }
            } else {
                onCompleteListener()
            }
        }
    }
}
