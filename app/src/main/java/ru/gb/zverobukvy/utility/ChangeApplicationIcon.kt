package ru.gb.zverobukvy.utility

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import ru.gb.zverobukvy.BuildConfig
import ru.gb.zverobukvy.data.theme_provider.Theme

object ChangeApplicationIcon {
    fun setIcon(context: Context, targetTheme: Theme) {
        for (value in Theme.values()) {
            val action = if (value == targetTheme) {
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