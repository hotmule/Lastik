package ru.hotmule.lastik.domain

import ru.hotmule.lastik.data.settings.PrefsStore

class ProfileInteractor(
    private val prefs: PrefsStore,
) {
    fun getName() = prefs.name ?: "Unknown"
}