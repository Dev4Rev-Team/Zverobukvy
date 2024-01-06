package ru.dev4rev.kids.zoobukvy.presentation.customview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import ru.dev4rev.kids.zoobukvy.utility.ui.dipToPixels

private const val OPEN_BEGIN = 0f
private const val CLOSE_BEGIN = 90f
private const val CLOSE_END = -90f
private const val OPEN_END = 0f
private const val INVISIBLE = 0f
private const val VISIBLE = 1f


fun createFlipAnimation(view: View, duration: Long): AnimatorSet {
    val animatorSet = AnimatorSet()
    val rotationClosing =
        ObjectAnimator.ofFloat(view, View.ROTATION_Y, OPEN_BEGIN, CLOSE_BEGIN).apply {
            this@apply.duration = duration / 2
        }
    val rotationOpening =
        ObjectAnimator.ofFloat(view, View.ROTATION_Y, CLOSE_END, OPEN_END).apply {
            this@apply.duration = duration / 2
        }
    animatorSet.playSequentially(rotationClosing, rotationOpening)
    return animatorSet
}

fun createScaleAnimation(view: View, x1: Float, x2: Float): AnimatorSet {
    val animatorSet = AnimatorSet()
    val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, x1, x2)
    val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, x1, x2)
    animatorSet.playTogether(scaleX, scaleY)
    return animatorSet
}

fun createAlphaShowAnimation(
    view: View,
    startDelay: Long,
    duration: Long,
    visible: Boolean = true
): ObjectAnimator {
    val objectAnimator = if (visible) {
        ObjectAnimator.ofFloat(view, View.ALPHA, INVISIBLE, VISIBLE)
    } else {
        ObjectAnimator.ofFloat(view, View.ALPHA, VISIBLE, INVISIBLE)
    }
    return objectAnimator.also {
        it.startDelay = startDelay
        it.duration = duration
        it.doOnStart {
            if (view.visibility != View.VISIBLE) {
                view.visibility = View.VISIBLE
            }
        }
    }
}


fun <T : View> createInSideAnimation(
    view: T,
    duration: Long,
    shift: Float,
    blockOut: (v: T) -> Unit
): AnimatorSet {
    val animatorSet = AnimatorSet()
    val animatorSetOut = AnimatorSet()
    val animatorSetIn = AnimatorSet()

    val pixelShift = view.context.dipToPixels(shift).toFloat()
    val moveOut =
        ObjectAnimator.ofFloat(view, View.TRANSLATION_X, pixelShift).apply {
            doOnEnd {
                blockOut(view)
            }
        }
    val alphaOut = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f)
    val scaleOut = createScaleAnimation(view, 1f, 0f)
    animatorSetOut.playTogether(moveOut, alphaOut, scaleOut)

    val moveIn = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, -pixelShift, 0f)
    val alphaIn = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f)
    val scaleIn = createScaleAnimation(view, 0f, 1f)
    animatorSetIn.playTogether(moveIn, alphaIn, scaleIn)

    animatorSetOut.interpolator = AccelerateInterpolator()
    animatorSetIn.interpolator = DecelerateInterpolator()
    animatorSet.playSequentially(animatorSetOut, animatorSetIn)
    animatorSet.duration = duration
    return animatorSet
}