package io.github.stream29.remenberingassistant.view.page

import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import io.github.stream29.remenberingassistant.model.ApplicationContext

@Composable
fun ApplicationContext.HelloPage() {
    MaterialTheme {
        Button(
            onClick = { navHostController.navigate("StreamChatPage") }
        ) {
            Text("Hello! Click to chat.")
        }
    }
}