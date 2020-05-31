package ru.hotmule.lastfmclient

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.hotmule.multitodo.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setEdgeToEdgeContent()
        setBottomNavigation()
    }

    private fun setEdgeToEdgeContent() {
        window?.apply {
            ContextCompat.getColor(context, android.R.color.transparent).also { transparent ->
                navigationBarColor = transparent
                statusBarColor = transparent
            }
            attributes = attributes?.apply {
                flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            }
            decorView.apply {
                systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                setOnApplyWindowInsetsListener { view, insets ->
                    view.updatePadding(top = insets.stableInsetTop)
                    insets
                }
            }
        }
    }

    private fun setBottomNavigation() {
        findViewById<BottomNavigationView>(R.id.bottom_navigation).apply {
            setupWithNavController(
                (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                    .navController
            )
            setOnApplyWindowInsetsListener { view, insets ->
                view.updatePadding(bottom = insets.stableInsetBottom)
                insets
            }
        }
    }
}