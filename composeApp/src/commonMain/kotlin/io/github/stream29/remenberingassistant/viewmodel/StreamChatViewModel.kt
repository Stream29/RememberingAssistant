package io.github.stream29.remenberingassistant.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.github.stream29.langchain4kt.streaming.asStreamChatModel
import io.github.stream29.remenberingassistant.apiProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class StreamChatViewModel : ViewModel() {
    private val model =
        apiProviders.firstNotNullOfOrNull { it.value.streamChatApiProvider }
            ?.asStreamChatModel()
            ?: throw IllegalStateException("No api auth available")
    val record = mutableStateListOf<String>()
    val currentStream = mutableStateListOf<String>()
    var inputText by mutableStateOf("")
    private val mutex = Mutex()

    fun chat(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            record += "User: $message"
            model.chat(message).collect {
                println("received: $it")
                mutex.withLock {
                    currentStream.add(it)
                }
            }
            record += "Model: ${currentStream.joinToString("")}"
            currentStream.clear()
        }
    }
}