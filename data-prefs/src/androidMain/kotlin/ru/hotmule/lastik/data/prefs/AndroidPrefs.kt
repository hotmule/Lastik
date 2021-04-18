package ru.hotmule.lastik.data.prefs

import android.content.Context
import com.russhwolf.settings.AndroidSettings

fun androidPrefs(
    context: Context
) = AndroidSettings(
    delegate = context.getSharedPreferences(
        PrefsStore.USER_DATA_PREFS,
        Context.MODE_PRIVATE
    )
)