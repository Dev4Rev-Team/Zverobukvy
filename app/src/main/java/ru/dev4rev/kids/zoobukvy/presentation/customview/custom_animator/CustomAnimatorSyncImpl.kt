package ru.dev4rev.kids.zoobukvy.presentation.customview.custom_animator

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator

class CustomAnimatorSyncImpl : CustomAnimatorSync {
    private val animatorSyncable = mutableSetOf<CustomAnimatorSyncable>()
    val needStartAnimators = mutableSetOf<CustomAnimatorSyncable>()
    val needStopAnimators = mutableSetOf<CustomAnimatorSyncable>()
    val enableAnimators = mutableSetOf<CustomAnimatorSyncable>()
    private var keeperListenerObjectAnimator: ObjectAnimator? = null
    private val listenerObjectAnimator = object : AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
        }

        override fun onAnimationEnd(animation: Animator) {
        }

        override fun onAnimationCancel(animation: Animator) {

        }

        override fun onAnimationRepeat(animation: Animator) {
            if (needStopAnimators.isNotEmpty()) {
                needStopAnimators.forEach {
                    if (it.animator == keeperListenerObjectAnimator) {
                        keeperListenerObjectAnimator?.removeAllListeners()
                        keeperListenerObjectAnimator = null
                    }
                    it.stopAnimator()
                    enableAnimators.remove(it)
                }
                needStopAnimators.clear()
            }

            if (needStartAnimators.isNotEmpty()) {
                needStartAnimators.forEach {
                    it.startAnimator()
                }
                needStartAnimators.clear()
            }

            if (keeperListenerObjectAnimator == null) {
                keeperListenerObjectAnimator = enableAnimators.firstOrNull()?.animator
                keeperListenerObjectAnimator?.addListener(this)
            }
        }

    }

    private fun listenerChange(
        customAnimatorSyncable: CustomAnimatorSyncable,
        enable: Boolean
    ): Unit {

        if (enable) {
            enableAnimators.add(customAnimatorSyncable)
            if (keeperListenerObjectAnimator == null) {
                keeperListenerObjectAnimator = customAnimatorSyncable.animator
                keeperListenerObjectAnimator?.addListener(listenerObjectAnimator)
                customAnimatorSyncable.startAnimator()
            } else {
                needStartAnimators.add(customAnimatorSyncable)
            }
        } else {
            needStopAnimators.add(customAnimatorSyncable)
        }
    }

    override fun add(customAnimatorSyncable: CustomAnimatorSyncable) {
        animatorSyncable.add(customAnimatorSyncable)
        customAnimatorSyncable.listenerChange = ::listenerChange
        customAnimatorSyncable.isSyncable = true
    }

    override fun clear() {
        animatorSyncable.forEach {
            it.isSyncable = false
            it.listenerChange = null
        }
        keeperListenerObjectAnimator = null
        enableAnimators.clear()
        needStartAnimators.clear()
        needStopAnimators.clear()
        animatorSyncable.clear()
    }
}