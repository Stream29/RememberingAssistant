package io.github.stream29.remenberingassistant.model

import io.github.stream29.remenberingassistant.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}