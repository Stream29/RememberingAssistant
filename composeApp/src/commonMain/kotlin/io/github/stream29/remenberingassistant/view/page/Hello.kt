package io.github.stream29.remenberingassistant.view.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.stream29.remenberingassistant.model.ApplicationContext
import io.github.stream29.remenberingassistant.model.navigate

@Composable
fun ApplicationContext.HelloPage() {
    MaterialTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.background(Color.Gray).padding(50.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Remembering Assistant",
                    modifier = Modifier.background(Color.Gray),
                )
            }
            Button(onClick = { navigate(Page.StreamChatPage) }) {
                Text("Click to chat")
            }
            Button(onClick = { navigate(Page.MemoryEditPage) }) {
                Text("Manage memory")
            }
            Button(onClick = { navigate(Page.ApiConfigEditPage) }) {
                Text("Manage API Auth and model setting")
            }
        }
    }
}