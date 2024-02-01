package ru.dev4rev.kids.zoobukvy.presentation.customview

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import androidx.core.animation.doOnEnd

class CustomAnimatorSoaring(
    view: View,
    durationAnimation: Long = DURATION
) {
    var enable: Boolean = false
        set(value) {
            if (field == value) return
            if (value) {
                animatorUp.apply {
                    doOnEnd {
                        animatorSoaring.currentPlayTime = 0
                        if (field) animatorSoaring.start()
                    }
                    start()
                }
            } else {
                animatorSoaring.cancel()
                animatorDown.start()
            }
            field = value
        }

    private val animatorUp by lazy {
        ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat(View.SCALE_X, SCALE),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, SCALE)
        ).apply { duration = durationAnimation }
    }
    private val animatorDown by lazy {
        ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat(View.SCALE_X, SCALE_NORMAL),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, SCALE_NORMAL)
        ).apply { duration = durationAnimation }
    }
    private val animatorSoaring by lazy {
        ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat(View.SCALE_X, SCALE, SCALE_NORMAL),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, SCALE, SCALE_NORMAL)
        ).apply {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            duration = durationAnimation
        }
    }

    companion object {
        const val SCALE_NORMAL = 1f
        const val SCALE = 1.07f
        const val DURATION = 600L
    }

}