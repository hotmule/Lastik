package ru.hotmule.lastik.data.remote

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ru.hotmule.lastik.data.sdk.sdkDataModule
import ru.hotmule.lastik.data.remote.api.AuthApi
import ru.hotmule.lastik.data.remote.api.TrackApi
import ru.hotmule.lastik.data.remote.api.UserApi

val remoteDataModule = DI.Module("remoteData") {

    importOnce(sdkDataModule)

    bindSingleton { EngineFactory(di) }
    bindSingleton { LastikHttpClientFactory(instance(), instance()).create() }

    bindSingleton { AuthApi(instance()) }
    bindSingleton { UserApi(instance(), instance()) }
    bindSingleton { TrackApi(instance(), instance()) }
}