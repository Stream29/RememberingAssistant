package io.github.stream29.remenberingassistant.view.page

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
import io.github.stream29.remenberingassistant.model.ApplicationContext
import io.github.stream29.remenberingassistant.model.navigate
import io.github.stream29.remenberingassistant.recursiveMessage
import io.github.stream29.remenberingassistant.viewmodel.StreamChatViewModel

@Composable
fun ApplicationContext.SafeStreamChatPage() = MaterialTheme {
    val streamChatViewModelResult = remember { runCatching { StreamChatViewModel() } }
    streamChatViewModelResult.onSuccess {
        StreamChatPage(it)
    }.onFailure {
        FailDialog(it.recursiveMessage) { navigate(Page.HelloPage) }
    }
}

@Composable
fun ApplicationContext.StreamChatPage(streamChatViewModel: StreamChatViewModel) = MaterialTheme {
    with(streamChatViewModel) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var onError by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf("") }
            if(onError)
                FailDialog(errorMessage) { onError = false }

            fun safeChat(input: String) {
                chat(input).invokeOnCompletion {
                    it?.let {
                        onError = true
                        errorMessage = it.recursiveMessage
                    }
                }
                inputText = ""
            }
            LazyColumn(
                reverseLayout = true,
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                item {
                    if (currentStream.isNotEmpty()) {
                        Text("Model: ${currentStream.joinToString("")}")
                        Divider()
                    }
                }
                record.toList().asReversed().forEach {
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
                    value = inputText,
                    modifier = Modifier.fillMaxWidth(0.7f).fillMaxHeight().scrollable(
                        orientation = Orientation.Vertical,
                        state = rememberScrollState(),
                    ),
                    onValueChange = {
                        if (it.contains("\n"))
                            safeChat(inputText)
                        else
                            inputText = it
                    }
                )
                Button(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    onClick = {
                        safeChat(inputText)
                    }
                ) {
                    Text("Chat")
                }
            }
        }
    }
}
