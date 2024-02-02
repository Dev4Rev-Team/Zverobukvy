package ru.dev4rev.kids.zoobukvy.presentation.customview.custom_animator

import android.animation.ObjectAnimator

interface CustomAnimatorSyncable {
    var isSyncable: Boolean
    var listenerChange: ((customAnimator: CustomAnimatorSyncable, enable: Boolean) -> Unit)?
    val animator:ObjectAnimator
    fun startAnimator()
    fun stopAnimator()
}
