package ru.hotmule.lastik.domain

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import ru.hotmule.lastik.data.local.TrackQueries
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.api.UserApi

class ScrobblesInteractor(
    private val prefs: PrefsStore,
    private val api: UserApi,
    private val dao: TrackQueries
) {
    suspend fun refreshScrobbles() {

        api.getRecentTracks(prefs.name).also { scrobbles ->
            dao.transaction {
                scrobbles?.recent?.tracks?.forEach {
                    dao.insert(
                        it.name,
                        it.artist?.title,
                        it.album?.title,
                        it.images?.get(0)?.url
                    )
                }
            }
        }
    }

    fun observeScrobbles() = dao.selectAll().asFlow().mapToList()
}