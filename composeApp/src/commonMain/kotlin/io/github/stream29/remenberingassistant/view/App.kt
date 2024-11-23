package io.github.stream29.remenberingassistant.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                    if(contextViewModel.currentStream.isNotEmpty()) {
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
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp, max = 80.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                TextField(
                    value = chatMessage,
                    modifier = Modifier.fillMaxWidth(0.7f).fillMaxHeight(),
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