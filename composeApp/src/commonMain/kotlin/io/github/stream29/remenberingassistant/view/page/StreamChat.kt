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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.onKeyEvent
import io.github.stream29.remenberingassistant.model.ApplicationContext
import io.github.stream29.remenberingassistant.model.back
import io.github.stream29.remenberingassistant.model.navigate
import io.github.stream29.remenberingassistant.recursiveMessage
import io.github.stream29.remenberingassistant.view.component.ChatHistoryItem
import io.github.stream29.remenberingassistant.view.component.FailDialog
import io.github.stream29.remenberingassistant.view.component.SelectableStatus
import io.github.stream29.remenberingassistant.viewmodel.StreamChatViewModel

@Composable
fun ApplicationContext.SafeStreamChatPage(): Unit = MaterialTheme {
    var streamChatViewModelResult by remember { mutableStateOf(runCatching { StreamChatViewModel() }) }
    streamChatViewModelResult.onSuccess {
        StreamChatPage(it)
    }.onFailure {
        streamChatViewModelResult = runCatching { StreamChatViewModel() }.onSuccess {
            StreamChatPage(it)
        }.onFailure { FailDialog(it.recursiveMessage) { navigate(Page.ApiConfigEditPage) } }
    }
}

@Composable
fun ApplicationContext.StreamChatPage(streamChatViewModel: StreamChatViewModel) = with(streamChatViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (onError)
            FailDialog(errorMessage) { onError = false }
        TopAppBar(modifier = Modifier.fillMaxWidth().weight(0.1f)) {
            Button(onClick = { back() }) { Text("<") }
            Text(text = "Stream Chat", modifier = Modifier.weight(1f))
            SelectableStatus(
                expandedState = dropdownExpandedState,
                selected = { Text("Using API: $currentApiAuthName") },
            ) {
                apiProviders.forEach { (name, auth) ->
                    DropdownMenuItem(
                        onClick = {
                            currentNamedApiAuth = name to auth
                            dropdownExpandedState.value = false
                        }
                    ) {
                        Text(name)
                    }
                }
            }
        }
        LazyColumn(
            reverseLayout = true,
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            currentStream.takeIf { it.isNotEmpty() }?.let { ChatHistoryItem("Model", it) }
            record.toList().asReversed().forEach { ChatHistoryItem(it.first, it.second) }
        }
        Row(
            modifier = Modifier.fillMaxWidth().weight(0.2f),
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
                        chat()
                    else
                        inputText = it
                }
            )
            Button(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                onClick = {
                    chat()
                }
            ) {
                Text("Chat")
            }
        }
    }
}

