package io.github.stream29.remenberingassistant.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import io.github.stream29.langchain4kt.streaming.asStreamChatModel
import io.github.stream29.remenberingassistant.streamChatApiProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ContextViewModel : ViewModel() {
    private val model = streamChatApiProvider.asStreamChatModel()
    val record = mutableStateListOf<String>()
    val currentStream = mutableStateListOf<String>()
    private val mutex = Mutex()

    fun chat(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            record += "User: $message"
            runCatching {
                model.chat(message).collect {
                    println("received: $it")
                    mutex.withLock {
                        currentStream.add(it)
                    }
                }
                record += "Model: ${currentStream.joinToString("")}"
                currentStream.clear()
            }.onFailure {
                record += "Error: ${it.stackTraceToString()}"
            }
        }
    }
}