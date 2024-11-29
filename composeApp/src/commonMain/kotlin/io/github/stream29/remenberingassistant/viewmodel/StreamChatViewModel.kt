package io.github.stream29.remenberingassistant.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.github.stream29.langchain4kt.core.input.Context
import io.github.stream29.langchain4kt.streaming.SimpleStreamChatModel
import io.github.stream29.langchain4kt.streaming.StreamChatApiProvider
import io.github.stream29.langchain4kt.streaming.StreamChatModel
import io.github.stream29.remenberingassistant.Global
import io.github.stream29.remenberingassistant.recursiveMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class StreamChatViewModel(
    val context: Context = Context()
) : ViewModel() {
    init {
        println("StreamChatViewModel init")
    }

    val apiProviders
        get() = Global.apiProviders
    var currentApiProvider =
        apiProviders.firstNotNullOfOrNull { it.key to it.value.streamChatApiProvider }
            ?: throw IllegalStateException("No api auth available")
        set(value) {
            field = value
            currentApiProviderName = value.first
            if (currentModelLock.isLocked) {
                nextApiProvider = value.second
            } else {
                currentModel = SimpleStreamChatModel(value.second, context)
                println("api changed")
            }
        }
    private var currentModel: StreamChatModel = SimpleStreamChatModel(currentApiProvider.second, context)
    var currentApiProviderName by mutableStateOf(currentApiProvider.first)
    val record = mutableStateListOf<String>()
    var dropdownExpandedState = mutableStateOf(false)
    var currentStream by mutableStateOf<String>("")
    var inputText by mutableStateOf("")
    var onError by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    @Volatile
    private var nextApiProvider: StreamChatApiProvider<*>? = null
    private val currentModelLock = Mutex()
    private val currentStreamLock = Mutex()

    fun chat() = CoroutineScope(Dispatchers.IO).launch {
        currentModelLock.withLock {
            val message = inputText
            inputText = ""
            runCatching {
                record += "User: \n$message"
                currentStream += "Model: \n"
                currentModel.chat(message).collect {
                    println("received: $it")
                    currentStreamLock.withLock {
                        currentStream += it
                    }
                }
                record += currentStream
                currentStream = ""
            }.onFailure {
                onError = true
                errorMessage = it.recursiveMessage
                currentStream = ""
            }
            nextApiProvider?.let {
                nextApiProvider = null
                currentModel = SimpleStreamChatModel(it, context)
                println("api changed")
            }
        }
    }
}