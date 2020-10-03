package ru.hotmule.lastik.domain

import ru.hotmule.lastik.data.local.ProfileQueries
import ru.hotmule.lastik.data.prefs.PrefsStore

class SignOutInteractor(
    private val prefs: PrefsStore,
    private val queries: ProfileQueries
) {
    fun signOut() {
        prefs.clear()
        queries.deleteAll()
    }
}