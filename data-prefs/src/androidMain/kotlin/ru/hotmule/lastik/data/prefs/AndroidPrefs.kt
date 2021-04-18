package ru.hotmule.lastik.data.prefs

import android.content.Context
import com.russhwolf.settings.AndroidSettings

@Suppress("FunctionName")
fun AndroidPrefs(
    context: Context
) = AndroidSettings(
    delegate = context.getSharedPreferences(
        PrefsStore.USER_DATA_PREFS,
        Context.MODE_PRIVATE
    )
)