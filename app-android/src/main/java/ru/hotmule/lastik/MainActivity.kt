package ru.hotmule.lastik

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Browser
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.feature.root.RootComponentImpl
import ru.hotmule.lastik.ui.compose.root.RootContent
import ru.hotmule.lastik.ui.compose.theme.DarkColors
import ru.hotmule.lastik.ui.compose.theme.LightColors

class MainActivity : AppCompatActivity() {

    lateinit var rootComponent: RootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        val sdk = Sdk.create(
            this,
            BuildConfig.DEBUG,
            BuildConfig.API_KEY,
            BuildConfig.SECRET
        )
        */

        //WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MaterialTheme(
                colors = if (isSystemInDarkTheme()) DarkColors else LightColors
            ) {
                rootComponent = rememberRootComponent {
                    RootComponentImpl(
                        componentContext = it,
                        storeFactory = DefaultStoreFactory,
                        webBrowser = { url ->
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(url)
                                ).putExtra(
                                    Browser.EXTRA_APPLICATION_ID,
                                    packageName
                                ).addFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                                )
                            )
                        }
                    )
                }
                RootContent(rootComponent)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.data?.let {
            rootComponent.onTokenUrlReceived(it.toString())
        }
    }
}