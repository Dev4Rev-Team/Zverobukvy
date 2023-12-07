package ru.gb.zverobukvy.utility

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import ru.gb.zverobukvy.BuildConfig

object ChangeApplicationIcon {
    enum class IconColour { BASE, FOX, NEW_YEAR }

    fun setIcon(context: Context, targetColour: IconColour) {
        for (value in IconColour.values()) {
            val action = if (value == targetColour) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            }
            context.packageManager.setComponentEnabledSetting(
                ComponentName(
                    BuildConfig.APPLICATION_ID,
                    "${BuildConfig.APPLICATION_ID}.${value.name}"
                ),
                action, PackageManager.DONT_KILL_APP
            )
        }
    }
}