package ru.gb.zverobukvy.presentation.customview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.PointF
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import ru.gb.zverobukvy.utility.ui.dipToPixels

private const val OPEN_BEGIN = 0f
private const val CLOSE_BEGIN = 90f
private const val CLOSE_END = 270f
private const val OPEN_END = 360f
private const val INVISIBLE = 0f
private const val VISIBLE = 1f


fun createFlipAnimation(view: View, changeFace: (() -> Unit)? = null): AnimatorSet {
    val animatorSet = AnimatorSet()

    val rotationClosing =
        ObjectAnimator.ofFloat(view, View.ROTATION_Y, OPEN_BEGIN, CLOSE_BEGIN).apply {
            doOnEnd {
                changeFace?.invoke()
            }
        }
    val rotationOpening = ObjectAnimator.ofFloat(view, View.ROTATION_Y, CLOSE_END, OPEN_END)
    animatorSet.playSequentially(rotationClosing, rotationOpening)
    return animatorSet
}

fun createScaleAnimation(view: View, vararg value: Float): AnimatorSet {
    val animatorSet = AnimatorSet()
    val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, *value)
    val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, *value)
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


fun createMoveAnimation(view: View, pos1: PointF, pos2: PointF): AnimatorSet {
    val animatorSet = AnimatorSet()
    val moveX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, pos1.x, pos2.x)
    val moveY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, pos1.y, pos2.y)
    val scaleAnimation = createScaleAnimation(view, 1f, 1.3f, 1f).apply {
        duration = 50L
    }
//    todo синхронизация с вьюмоделью
//    animatorSet.play(scaleAnimation).after(moveX).with(moveY)
    animatorSet.playTogether(moveX, moveY)
    return animatorSet
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