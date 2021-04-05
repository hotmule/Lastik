package ru.hotmule.lastik.feature.root

import ru.hotmule.lastik.feature.auth.Auth

interface LastikRoot {

    sealed class Child {
        data class AuthChild(val companion: Auth) : Child()
        data class MainChild(val companion: Main) : Child()
    }

    interface Dependencies {

    }
}