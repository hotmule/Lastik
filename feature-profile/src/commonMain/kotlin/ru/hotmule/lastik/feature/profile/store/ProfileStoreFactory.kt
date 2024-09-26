package ru.hotmule.lastik.feature.profile.store

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
import ru.hotmule.lastik.data.sdk.prefs.PrefsStore
import ru.hotmule.lastik.utils.AppCoroutineDispatcher
import ru.hotmule.lastik.utils.Formatter
import ru.hotmule.lastik.feature.profile.ProfileComponent

internal class ProfileStoreFactory(
    private val storeFactory: StoreFactory,
    private val profileQueries: ProfileQueries,
    private val friendQueries: FriendQueries,
    private val prefs: PrefsStore,
    private val api: UserApi,
) {
    fun create(): ProfileStore = object : ProfileStore,
        Store<ProfileStore.Intent, ProfileStore.State, Nothing> by storeFactory.create(
            name = ProfileStore::class.simpleName,
            initialState = ProfileStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private inner class ExecutorImpl :
        CoroutineExecutor<ProfileStore.Intent, Unit, ProfileStore.State, ProfileStore.Result, Nothing>(
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

        override fun executeIntent(intent: ProfileStore.Intent) {
            scope.launch {
                when (intent) {
                    ProfileStore.Intent.Refresh -> refreshInfo()
                    ProfileStore.Intent.LoadMoreFriends -> {
                        val profileId = profileQueries.getProfile().executeAsOneOrNull()?.id
                        loadFriends(false, profileId)
                    }
                    ProfileStore.Intent.LogOut -> dispatch(ProfileStore.Result.LoggingOut)
                    ProfileStore.Intent.LogOutCancel -> dispatch(ProfileStore.Result.LoggingOutCanceled)
                    ProfileStore.Intent.LogOutConfirm -> prefs.clear()
                }
            }
        }

        private suspend fun collectProfile() {
            profileQueries.getProfile()
                .asFlow()
                .mapToOneOrNull(AppCoroutineDispatcher.IO)
                .collect {
                    dispatch(
                        ProfileStore.Result.ProfileReceived(
                            ProfileComponent.Profile(
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
                            ProfileStore.Result.FriendsReceived(
                                friends.map {
                                    ProfileComponent.Profile(
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

                        with(friendQueries) {
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

    object ReducerImpl : Reducer<ProfileStore.State, ProfileStore.Result> {
        override fun ProfileStore.State.reduce(msg: ProfileStore.Result): ProfileStore.State = when (msg) {
            is ProfileStore.Result.ProfileReceived -> copy(info = msg.profile)
            is ProfileStore.Result.FriendsReceived -> copy(friends = msg.friends)
            is ProfileStore.Result.MoreFriendsLoading -> copy(isMoreFriendsLoading = msg.isLoading)
            is ProfileStore.Result.LoggingOut -> copy(isLogOutShown = true)
            is ProfileStore.Result.LoggingOutCanceled -> copy(isLogOutShown = false)
        }
    }
}
