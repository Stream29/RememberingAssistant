package io.github.stream29.remenberingassistant

val Throwable.recursiveMessage: String
    get() = buildString {
        var currentThrowable: Throwable? = this@recursiveMessage
        while (currentThrowable != null) {
            appendLine(currentThrowable.message ?: "unknown error")
            currentThrowable = currentThrowable.cause
        }
    }