package ru.dev4rev.kids.zoobukvy.utility

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import kotlin.time.Duration

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

/**
 * Метод для конвертации продолжительности игры в строку
 */
fun Duration.formatToString(): String =
    this.toComponents { hours, minutes, seconds, _ ->
        if (hours == 0L)
            "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        else
            "$hours:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }
