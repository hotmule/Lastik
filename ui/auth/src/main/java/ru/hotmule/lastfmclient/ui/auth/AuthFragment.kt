package ru.hotmule.lastfmclient.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : Fragment(R.layout.fragment_auth) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wv_auth.loadUrl(
            "https://www.last.fm/api/auth?api_key=83e8a034819711968af5e5d5cf7cc4bd"
        )
    }
}