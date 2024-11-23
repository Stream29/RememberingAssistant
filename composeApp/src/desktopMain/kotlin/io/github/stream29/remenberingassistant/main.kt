package io.github.stream29.remenberingassistant

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.stream29.remenberingassistant.view.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CmpPackagingDemo",
    ) {
        App()
    }
}