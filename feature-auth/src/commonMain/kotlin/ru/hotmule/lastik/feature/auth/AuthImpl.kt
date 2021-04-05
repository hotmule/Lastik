package ru.hotmule.lastik.feature.auth

import com.arkivanov.decompose.ComponentContext

class AuthImpl(
    componentContext: ComponentContext
) : Auth, ComponentContext by componentContext {

}