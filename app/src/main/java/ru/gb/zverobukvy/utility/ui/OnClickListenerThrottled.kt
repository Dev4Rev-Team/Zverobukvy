package ru.gb.zverobukvy.utility.ui

import android.os.SystemClock
import android.view.View

class OnClickListenerThrottled(private val block: () -> Unit, private val wait: Long) : View.OnClickListener {

    private var lastClickTime = 0L

    override fun onClick(view: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < wait) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()

        block()
    }
}

/**
 * A throttled click listener that only invokes [block] at most once per every [wait] milliseconds.
 */
fun View.setOnClickListenerThrottled(wait: Long = DURATION, block: () -> Unit) {
    setOnClickListener(OnClickListenerThrottled(block, wait))
}

private const val DURATION = 1000L
