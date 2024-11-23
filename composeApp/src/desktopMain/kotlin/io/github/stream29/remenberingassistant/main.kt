package io.github.stream29.remenberingassistant

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CmpPackagingDemo",
    ) {
        App()
    }
}