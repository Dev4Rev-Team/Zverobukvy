import android.app.Activity
import ru.dev4rev.kids.zoobukvy.presentation.review.Review
import ru.rustore.sdk.core.tasks.OnCompleteListener
import ru.rustore.sdk.review.RuStoreReviewManagerFactory
import ru.rustore.sdk.review.model.ReviewInfo

class ReviewImpl : Review {
    override fun request(activity: Activity, onCompleteListener: () -> Unit) {
        val manager = RuStoreReviewManagerFactory.create(activity)
        manager.requestReviewFlow().addOnCompleteListener(object : OnCompleteListener<ReviewInfo> {
            override fun onFailure(throwable: Throwable) {
                onCompleteListener()
            }

            override fun onSuccess(reviewInfo: ReviewInfo) {
                manager.launchReviewFlow(reviewInfo).addOnCompleteListener(object: OnCompleteListener<Unit> {
                    override fun onFailure(throwable: Throwable) {
                        onCompleteListener()
                    }
                    override fun onSuccess(result: Unit) {
                        onCompleteListener()
                    }
                })
            }
        })
    }
}