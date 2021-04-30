package ru.hotmule.lastik.feature.profile.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hotmule.lastik.data.local.FriendQueries
import ru.hotmule.lastik.data.local.ProfileQueries
import ru.hotmule.lastik.data.prefs.PrefsStore
import ru.hotmule.lastik.data.remote.LastikHttpClient
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.remote.entities.User
import ru.hotmule.lastik.feature.profile.ProfileComponent
import ru.hotmule.lastik.feature.profile.store.ProfileStore.*
import ru.hotmule.lastik.utils.AppCoroutineDispatcher
import ru.hotmule.lastik.utils.Formatter

class ProfileStoreFactory(
    private val storeFactory: StoreFactory,
    private val profileQueries: ProfileQueries,
    private val friendQueries: FriendQueries,
    private val prefsStore: PrefsStore,
    private val api: UserApi
) {
    fun create(): ProfileStore = object : ProfileStore, Store<Intent, State, Nothing> by storeFactory.create(
        name = ProfileStore::class.simpleName,
        initialState = State(),
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    private inner class ExecutorImpl : SuspendExecutor<Intent, Unit, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {
        override suspend fun executeAction(
            action: Unit,
            getState: () -> State
        ) {
            withContext(AppCoroutineDispatcher.Main) {
                launch { collectProfile() }
                launch { collectFriends() }
                launch { refreshProfile() }
            }
        }

        private suspend fun collectProfile() {
            profileQueries.getProfile()
                .asFlow()
                .mapToOneOrNull(AppCoroutineDispatcher.IO)
                .collect {
                    dispatch(
                        Result.ProfileReceived(
                            ProfileComponent.User(
                                username = it?.userName ?: "",
                                image = it?.lowResImage ?: LastikHttpClient.defaultImageUrl,
                                playCount = Formatter.numberToCommasString(it?.playCount),
                                scrobblingSince = Formatter.utsDateToString(it?.registerDate, "d MMMM yyyy")
                            )
                        )
                    )
                }
        }

        private suspend fun collectFriends() {
            friendQueries.friendsData(1)
                .asFlow()
                .mapToList(AppCoroutineDispatcher.IO)
                .collect { friends ->
                    dispatch(
                        Result.FriendsReceived(
                            friends.map {
                                ProfileComponent.User(
                                    username = it.userName ?: "",
                                    image = it.lowResImage ?: LastikHttpClient.defaultImageUrl
                                )
                            }
                        )
                    )
                }
        }

        private suspend fun refreshProfile() {
            withContext(AppCoroutineDispatcher.IO) {
                val profileRequest = async { api.getInfo() }
                val friendsRequest = async { api.getFriends(1) }

                val profile = profileRequest.await()?.user
                val friends = friendsRequest.await()?.friends?.user

                friendQueries.transaction {
                    val profileId = insertUser(profile)
                    insertFriends(profileId, friends)
                }
            }
        }

        override suspend fun executeIntent(
            intent: Intent,
            getState: () -> State
        ) {
            when (intent) {
                Intent.RefreshProfile -> { }
                Intent.LoadMoreFriends -> { }
                Intent.LogOut -> prefsStore.clear()
            }
        }

        private fun insertUser(user: User?): Long? {
            with(profileQueries) {
                upsert(
                    userName = user?.nickname,
                    realName = user?.realName,
                    lowResImage = user?.image?.get(2)?.url,
                    highResImage = user?.image?.get(3)?.url,
                    playCount = user?.playCount,
                    registerDate = user?.registered?.time?.toLongOrNull()
                )
                return getId(user?.nickname).executeAsOneOrNull()
            }
        }

        private fun insertFriends(
            profileId: Long?,
            friends: List<User>?
        ) {
            if (profileId != null) {
                with(friendQueries) {
                    deleteAll(profileId)
                    friends?.forEach {
                        insertUser(it)?.also { friendId -> insert(friendId, profileId) }
                    }
                }
            }
        }
    }

    object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State = when (result) {
            is Result.ProfileReceived -> copy(profile = result.profile)
            is Result.FriendsReceived -> copy(friends = result.friends)
            is Result.MoreFriendsLoading -> copy(isMoreFriendsLoading = result.isLoading)
        }
    }
}