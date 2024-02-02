package ru.dev4rev.kids.zoobukvy.presentation.customview.custom_animator

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import androidx.core.animation.doOnEnd

class CustomAnimatorSoaring(
    view: View
) : CustomAnimator, CustomAnimatorSyncable {
    override var isSyncable: Boolean = false
    override var listenerChange: ((customAnimator: CustomAnimatorSyncable, enable: Boolean) -> Unit)? =
        null
    override val animator: ObjectAnimator
        get() = animatorSoaring

    override var enable: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            if (isSyncable) {
                listenerChange?.invoke(this, field)
            } else {
                if (value) {
                    startAnimator()
                } else {
                    stopAnimator()
                }
            }
        }

    override fun end() {
        if (enable) {
            animatorSoaring.end()
        }
    }

    override fun stopAnimator() {
        animatorSoaring.cancel()
        animatorDown.start()
    }

    override fun startAnimator() {
        animatorSoaring.currentPlayTime = 0
        if (enable) animatorSoaring.start()
    }

    private val animatorDown by lazy {
        ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat(View.SCALE_X, SCALE_NORMAL),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, SCALE_NORMAL)
        ).apply { duration = DURATION_ACTION }
    }
    private val animatorSoaring by lazy {
        ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat(View.SCALE_X, SCALE_NORMAL, SCALE, SCALE_NORMAL),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, SCALE_NORMAL, SCALE, SCALE_NORMAL)
        ).apply {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            duration = DURATION_SOARING
        }
    }


    companion object {
        const val SCALE_NORMAL = 1f
        const val SCALE = 1.07f
        const val DURATION_ACTION = 300L
        const val DURATION_SOARING = 1200L
    }

}