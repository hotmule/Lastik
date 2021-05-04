package ru.hotmule.lastik

import androidx.compose.web.css.padding
import androidx.compose.web.css.px
import androidx.compose.web.elements.Div
import androidx.compose.web.elements.Text
import androidx.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        Div(style = { padding(25.px) }) {
            Text("Test compose web page")
        }
    }
}