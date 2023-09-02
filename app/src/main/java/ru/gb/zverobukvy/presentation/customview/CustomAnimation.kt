package ru.gb.zverobukvy.presentation.customview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart

private const val OPEN_BEGIN = 0f
private const val CLOSE_BEGIN = 90f
private const val CLOSE_END = 270f
private const val OPEN_END = 360f
private const val INVISIBLE = 0f
private const val VISIBLE = 1f


fun createFlipAnimation(view: View, changeFace: (() -> Unit)? = null): AnimatorSet {
    val animatorSet = AnimatorSet()

    val rotationClosing =
        ObjectAnimator.ofFloat(view, CardView.ROTATION_Y, OPEN_BEGIN, CLOSE_BEGIN).apply {
            doOnEnd {
                changeFace?.invoke()
            }
        }
    val rotationOpening = ObjectAnimator.ofFloat(view, CardView.ROTATION_Y, CLOSE_END, OPEN_END)
    animatorSet.playSequentially(rotationClosing, rotationOpening)
    return animatorSet
}

fun createScaleAnimation(view: View, x1: Float, x2: Float): AnimatorSet {
    val animatorSet = AnimatorSet()
    val scaleX = ObjectAnimator.ofFloat(view, CardView.SCALE_X, x1, x2)
    val scaleY = ObjectAnimator.ofFloat(view, CardView.SCALE_Y, x1, x2)
    animatorSet.playTogether(scaleX, scaleY)
    return animatorSet
}

fun createAlphaShowAnimation(view: View, startDelay: Long, duration: Long): ObjectAnimator {
    return ObjectAnimator.ofFloat(view, View.ALPHA, INVISIBLE, VISIBLE).also {
        it.startDelay = startDelay
        it.duration = duration
        it.doOnStart {
            view.visibility = View.VISIBLE
        }
    }
}