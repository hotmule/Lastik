package ru.hotmule.lastfmclient

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainScreen() }
    }

    /*
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
     */
}