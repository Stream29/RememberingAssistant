package io.github.stream29.remenberingassistant.view

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.stream29.remenberingassistant.viewmodel.ContextViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val contextViewModel = remember { ContextViewModel() }
        var chatMessage by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                reverseLayout = true,
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                item {
                    if (contextViewModel.currentStream.isNotEmpty()) {
                        Text("Model: ${contextViewModel.currentStream.joinToString("")}")
                        Divider()
                    }
                }
                contextViewModel.record.toList().asReversed().forEach {
                    item {
                        Text(it)
                        Divider()
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().weight(0.1f),
                verticalAlignment = Alignment.Bottom
            ) {
                TextField(
                    value = chatMessage,
                    modifier = Modifier.fillMaxWidth(0.7f).fillMaxHeight().scrollable(
                        orientation = Orientation.Vertical,
                        state = rememberScrollState(),
                    ),
                    onValueChange = {
                        if (it.contains("\n")) {
                            contextViewModel.chat(chatMessage)
                            chatMessage = ""
                        } else {
                            chatMessage = it
                        }
                    }
                )
                Button(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    onClick = {
                        contextViewModel.chat(chatMessage)
                        chatMessage = ""
                    }
                ) {
                    Text("Chat")
                }
            }
        }
    }
}