package io.github.stream29.remenberingassistant

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.stream29.remenberingassistant.view.App
import java.io.PrintStream

fun main() = application {
    System.setOut(PrintStream(System.out, false, "UTF-8"))
    Window(
        onCloseRequest = ::exitApplication,
        title = "Remembering Assistant",
    ) {
        App()
    }
}