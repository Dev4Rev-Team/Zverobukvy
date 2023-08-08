package ru.gb.zverobukvy.presentation.customview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd

fun createFlipAnimation(view: View, changeFace: (() -> Unit)? = null): AnimatorSet {
    val animatorSet = AnimatorSet()
    val rotationClosing = ObjectAnimator.ofFloat(view, CardView.ROTATION_Y, 0f, 90f).apply {
        doOnEnd {
            changeFace?.invoke()
        }
    }
    val rotationOpening = ObjectAnimator.ofFloat(view, CardView.ROTATION_Y, 270f, 360f)
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