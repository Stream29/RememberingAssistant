package io.github.stream29.remenberingassistant

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import io.github.stream29.langchain4kt.core.Respondent
import io.github.stream29.langchain4kt.core.dsl.add
import io.github.stream29.langchain4kt.core.input.Context
import io.github.stream29.langchain4kt.core.message.MessageSender
import io.github.stream29.langchain4kt.streaming.StreamChatModel
import io.github.stream29.langchain4kt.streaming.StreamRespondent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.reflect.KMutableProperty0

@Stable
data class RememberingStreamChatModel(
    val respondent: Respondent,
    val streamRespondent: StreamRespondent,
    val currentStreamDelegate: KMutableProperty0<String>,
    override val context: Context = Context(history = mutableStateListOf()),
) : StreamChatModel {
    private val memoryLock = Mutex()
    override val isReady by memoryLock::isLocked
    var currentStream by currentStreamDelegate

    override suspend fun chat(message: String): Flow<String> {
        memoryLock.withLock {
            println("get memory lock")
        }
        context.add { MessageSender.User.chat(message) }
        return streamRespondent.chat(responsePrompt(Global.memoryText, message))
            .onEach { currentStream += it }
            .onCompletion {
                CoroutineScope(Dispatchers.IO).launch {
                    memoryLock.withLock {
                        val memoryPrompt = getMemoryPrompt(Global.memoryText, message, currentStream)
                        println("memoryPrompt: $memoryPrompt")
                        Global.memoryText = respondent.chat(memoryPrompt)
                        context.add { MessageSender.Model.chat(currentStream) }
                        currentStream = ""
                        println("release memory lock")
                    }
                }
            }
    }
}