package io.github.stream29.remenberingassistant

import dev.langchain4j.model.dashscope.QwenChatModel
import dev.langchain4j.model.dashscope.QwenStreamingChatModel
import io.github.stream29.langchain4kt.api.langchain4j.Langchain4jChatApiProvider
import io.github.stream29.langchain4kt.api.langchain4j.Langchain4jStreamChatApiProvider

val apiKey = "API-KEY"

val apiProvider =
    Langchain4jChatApiProvider(
        QwenChatModel.builder()
            .apiKey(apiKey)
            .modelName("qwen-plus")
            .build()
    )

val streamChatApiProvider =
    Langchain4jStreamChatApiProvider(
        QwenStreamingChatModel.builder()
            .apiKey(apiKey)
            .modelName("qwen-plus")
            .build()
    )
