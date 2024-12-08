package io.github.stream29.remenberingassistant.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.github.stream29.langchain4kt.core.asRespondent
import io.github.stream29.langchain4kt.core.input.Context
import io.github.stream29.langchain4kt.core.message.Message
import io.github.stream29.langchain4kt.streaming.asStreamRespondent
import io.github.stream29.remenberingassistant.Global
import io.github.stream29.remenberingassistant.RememberingStreamChatModel
import io.github.stream29.remenberingassistant.model.ApiAuth
import io.github.stream29.remenberingassistant.recursiveMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class StreamChatViewModel : ViewModel() {
    init {
        println("StreamChatViewModel init")
    }

    val apiProviders by Global::apiProviders
    var currentNamedApiAuth =
        apiProviders.asSequence()
            .firstOrNull { (name, _) -> name == Global.currentApiAuth }
            ?.let { it.key to it.value }
            ?: apiProviders.firstNotNullOfOrNull { it.key to it.value }
            ?: throw IllegalStateException("No api auth available, please add at least one")
        set(value) {
            field = value
            currentApiAuthName = value.first
            Global.currentApiAuth = value.first
            if (currentModelLock.isLocked) {
                nextApiAuth = value.second
            } else {
                currentApiAuth = value.second
                println("api changed")
            }
        }
    private var currentApiAuth = currentNamedApiAuth.second
        set(value) {
            field = value
            currentRespondent = value.chatApiProvider.asRespondent()
            currentStreamRespondent = value.streamChatApiProvider.asStreamRespondent()
            currentStreamChatModel = currentStreamChatModel.copy(
                respondent = currentRespondent,
                streamRespondent = currentStreamRespondent,
            )
        }

    var currentApiAuthName by mutableStateOf(currentNamedApiAuth.first)


    private var currentStreamRespondent = currentApiAuth.streamChatApiProvider.asStreamRespondent()
    private var currentRespondent = currentApiAuth.chatApiProvider.asRespondent()

    val history = mutableStateListOf<Message>()
    var currentStream by mutableStateOf("")

    var currentStreamChatModel = RememberingStreamChatModel(
        respondent = currentRespondent,
        streamRespondent = currentStreamRespondent,
        currentStreamDelegate = ::currentStream,
        context = Context(history = history)
    )

    var dropdownExpandedState = mutableStateOf(false)
    var inputText by mutableStateOf("")
    var onError by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    @Volatile
    private var nextApiAuth: ApiAuth? = null
    private val currentModelLock = Mutex()

    fun chat() = CoroutineScope(Dispatchers.IO).launch {
        currentModelLock.withLock {
            val message = inputText
            inputText = ""
            runCatching {
                currentStreamChatModel.chat(message).collect {}
            }.onFailure {
                onError = true
                errorMessage = it.recursiveMessage
                currentStream = ""
            }
            nextApiAuth?.let {
                nextApiAuth = null
                currentApiAuth = it
                println("api changed")
            }
        }
    }
}