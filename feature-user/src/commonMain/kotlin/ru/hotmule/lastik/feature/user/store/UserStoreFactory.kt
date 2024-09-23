package ru.hotmule.lastik.feature.user.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hotmule.lastik.data.local.FriendQueries
import ru.hotmule.lastik.data.local.ProfileQueries
import ru.hotmule.lastik.data.remote.api.UserApi
import ru.hotmule.lastik.data.remote.entities.User
import ru.hotmule.lastik.utils.AppCoroutineDispatcher
import ru.hotmule.lastik.utils.Formatter
import ru.hotmule.lastik.feature.user.UserComponent
import ru.hotmule.lastik.feature.user.store.UserStore.*

internal class UserStoreFactory(
    private val storeFactory: StoreFactory,
    private val profileQueries: ProfileQueries,
    private val friendQueries: FriendQueries,
    private val api: UserApi
) {
    fun create(): UserStore = object : UserStore, Store<Intent, State, Nothing> by storeFactory.create(
        name = UserStore::class.simpleName,
        initialState = State(),
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Result, Nothing>(
        AppCoroutineDispatcher.Main
    ) {
        override fun executeAction(action: Unit) {
            scope.launch {
                withContext(AppCoroutineDispatcher.Main) {
                    launch { collectProfile() }
                    launch { collectFriends() }
                    launch { refreshInfo() }
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            scope.launch {
                when (intent) {
                    Intent.Refresh -> refreshInfo()
                    Intent.LoadMoreFriends -> {
                        val profileId = profileQueries.getProfile().executeAsOneOrNull()?.id
                        loadFriends(false, profileId)
                    }
                }
            }
        }

        private suspend fun collectProfile() {
            profileQueries.getProfile()
                .asFlow()
                .mapToOneOrNull(AppCoroutineDispatcher.IO)
                .collect {
                    dispatch(
                        Result.ProfileReceived(
                            UserComponent.User(
                                username = it?.userName ?: "",
                                image = it?.lowResImage ?: UserApi.defaultImageUrl,
                                playCount = Formatter.numberToCommasString(it?.playCount),
                                scrobblingSince = Formatter.utsDateToString(
                                    it?.registerDate,
                                    "d MMMM yyyy"
                                )
                            )
                        )
                    )
                }
        }

        private suspend fun collectFriends() {
            profileQueries.getProfile().executeAsOneOrNull()?.let { profile ->
                friendQueries.friendsData(profile.id)
                    .asFlow()
                    .mapToList(AppCoroutineDispatcher.IO)
                    .collect { friends ->
                        dispatch(
                            Result.FriendsReceived(
                                friends.map {
                                    UserComponent.User(
                                        username = it.userName ?: "",
                                        image = it.lowResImage ?: UserApi.defaultImageUrl
                                    )
                                }
                            )
                        )
                    }
            }
        }

        private suspend fun refreshInfo() {
            withContext(AppCoroutineDispatcher.IO) {
                val profile = api.getInfo()?.user
                val profileId = insertUser(profile)
                loadFriends(true, profileId)
            }
        }

        private suspend fun loadFriends(
            isFirstPage: Boolean,
            profileId: Long?
        ) {
            withContext(AppCoroutineDispatcher.IO) {

                if (profileId != null) {

                    val friendsCount = friendQueries.getCount(profileId).executeAsOne().toInt()

                    if (isFirstPage || friendsCount.rem(50) == 0) {

                        val page = if (isFirstPage) 1 else friendsCount / 50 + 1
                        val friends = api.getFriends(page)?.friends?.user

                        with (friendQueries) {
                            transaction {
                                if (isFirstPage) deleteAll(profileId)
                                insertFriends(profileId, friends)
                            }
                        }
                    }
                }
            }
        }

        private fun insertFriends(
            profileId: Long?,
            friends: List<User>?
        ) {
            if (profileId != null) {
                friends?.forEach {
                    insertUser(it)?.also { friendId ->
                        friendQueries.insert(friendId, profileId)
                    }
                }
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
    }

    object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(msg: Result): State = when (msg) {
            is Result.ProfileReceived -> copy(info = msg.profile)
            is Result.FriendsReceived -> copy(friends = msg.friends)
            is Result.MoreFriendsLoading -> copy(isMoreFriendsLoading = msg.isLoading)
        }
    }
}