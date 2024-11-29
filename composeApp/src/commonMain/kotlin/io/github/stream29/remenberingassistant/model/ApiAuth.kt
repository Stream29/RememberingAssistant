package io.github.stream29.remenberingassistant.model

import dev.langchain4j.model.dashscope.QwenChatModel
import dev.langchain4j.model.dashscope.QwenStreamingChatModel
import io.github.stream29.langchain4kt.api.langchain4j.asChatApiProvider
import io.github.stream29.langchain4kt.api.langchain4j.asStreamChatApiProvider
import io.github.stream29.langchain4kt.core.ChatApiProvider
import io.github.stream29.langchain4kt.streaming.StreamChatApiProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ApiAuth {
    val chatApiProvider: ChatApiProvider<*>
    val streamChatApiProvider: StreamChatApiProvider<*>
}

@Suppress("unused")
@Serializable
@SerialName("ALIBABA_QWEN")
data class AlibabaQwenApiAuth(
    @SerialName("api_key")
    val apiKey: String,
    @SerialName("model_name")
    val modelName: String,
    val temperature: Float? = null,
    @SerialName("enable_search")
    val enableSearch: Boolean? = null,
    val stops: List<String>? = null,
    @SerialName("repetition_penalty")
    val repetitionPenalty: Float? = null,
): ApiAuth {
    override val chatApiProvider: ChatApiProvider<*>
        get() = QwenChatModel.builder()
            .apiKey(apiKey)
            .modelName(modelName)
            .temperature(temperature)
            .repetitionPenalty(repetitionPenalty)
            .enableSearch(enableSearch)
            .stops(stops)
            .build()
            .asChatApiProvider()
    override val streamChatApiProvider: StreamChatApiProvider<*>
        get() = QwenStreamingChatModel.builder()
            .apiKey(apiKey)
            .modelName(modelName)
            .temperature(temperature)
            .enableSearch(enableSearch)
            .stops(stops)
            .build()
            .asStreamChatApiProvider()
}