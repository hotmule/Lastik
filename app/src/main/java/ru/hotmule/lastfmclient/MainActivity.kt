package ru.hotmule.lastfmclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import ru.hotmule.lastfmclient.ui.auth.AuthFragment
import ru.hotmule.multitodo.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment

        navHostFragment.navController.apply {
            graph = createGraph(nav_graph.id, nav_graph.dest.auth) {
                fragment<AuthFragment>(nav_graph.dest.auth)
            }
        }
    }
}
