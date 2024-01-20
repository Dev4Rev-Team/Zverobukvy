package ru.dev4rev.kids.zoobukvy.utility

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import ru.dev4rev.kids.zoobukvy.configuration.Conf

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun isDebugRun(flag: Boolean, block: () -> Unit) {
    if (Conf.DEBUG && flag) {
        block.invoke()
    }
}