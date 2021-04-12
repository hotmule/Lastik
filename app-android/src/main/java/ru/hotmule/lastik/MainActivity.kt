package ru.hotmule.lastik

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import ru.hotmule.lastik.feature.root.RootComponent
import ru.hotmule.lastik.feature.root.RootComponentImpl
import ru.hotmule.lastik.ui.compose.root.RootContent
import ru.hotmule.lastik.ui.compose.theme.DarkColors
import ru.hotmule.lastik.ui.compose.theme.LightColors

class MainActivity : AppCompatActivity() {

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
                RootContent(
                    rememberRootComponent {
                        RootComponentImpl(
                            componentContext = it,
                            storeFactory = DefaultStoreFactory
                        )
                    }
                )
            }
        }
    }
}