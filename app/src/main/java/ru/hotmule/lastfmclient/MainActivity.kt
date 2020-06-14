package ru.hotmule.lastfmclient

import android.os.Bundle
import android.view.View.*
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.state
import androidx.core.view.updatePadding
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.layout.absolutePadding
import androidx.ui.layout.padding
import androidx.ui.material.*
import androidx.ui.unit.dp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainScreen() }
    }

    //Not working with compose
    private fun setEdgeToEdge() {
        window?.apply {
            attributes = attributes?.apply {
                flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            }
            decorView.apply {
                systemUiVisibility =
                    SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                setOnApplyWindowInsetsListener { view, insets ->
                    view.updatePadding(top = insets.stableInsetTop)
                    insets
                }
            }
        }
    }
}