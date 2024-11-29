package io.github.stream29.remenberingassistant.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.github.stream29.langchain4kt.core.asRespondent
import io.github.stream29.langchain4kt.streaming.asStreamRespondent
import io.github.stream29.remenberingassistant.Global
import io.github.stream29.remenberingassistant.memoryPrompt
import io.github.stream29.remenberingassistant.model.ApiAuth
import io.github.stream29.remenberingassistant.recursiveMessage
import io.github.stream29.remenberingassistant.responsePrompt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class StreamChatViewModel : ViewModel() {
    init {
        println("StreamChatViewModel init")
    }

    val apiProviders
        get() = Global.apiProviders
    var currentNamedApiAuth =
        apiProviders.asSequence()
            .firstOrNull { (name, auth) -> name == Global.apiAuthText }
            ?.let { it.key to it.value }
            ?: apiProviders.firstNotNullOfOrNull { it.key to it.value }
            ?: throw IllegalStateException("No api auth available")
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
        }
    private var currentRespondent = currentApiAuth.chatApiProvider.asRespondent()
    private var currentStreamRespondent = currentApiAuth.streamChatApiProvider.asStreamRespondent()
    var currentApiAuthName by mutableStateOf(currentNamedApiAuth.first)

    val record = mutableStateListOf<Pair<String, String>>()
    var dropdownExpandedState = mutableStateOf(false)
    var currentStream by mutableStateOf<String>("")
    var inputText by mutableStateOf("")
    var onError by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    @Volatile
    private var nextApiAuth: ApiAuth? = null
    private val currentModelLock = Mutex()
    private val currentStreamLock = Mutex()
    private val memoryLock = Mutex()

    fun chat() = CoroutineScope(Dispatchers.IO).launch {
        memoryLock.withLock {}
        currentModelLock.withLock {
            val message = inputText
            inputText = ""
            runCatching {
                record += "User" to message
                currentStreamRespondent.chat(responsePrompt(Global.memoryText, message)).collect {
                    println("received: $it")
                    currentStreamLock.withLock {
                        currentStream += it
                    }
                }
                val modelMessage = currentStream
                launch {
                    memoryLock.withLock {
                        val memoryPrompt1 = memoryPrompt(Global.memoryText, message, modelMessage)
                        println("memoryPrompt1: $memoryPrompt1")
                        Global.memoryText = currentRespondent.chat(memoryPrompt1)
                    }
                }
                record += "Model" to currentStream
                currentStream = ""
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