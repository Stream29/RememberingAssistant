package io.github.stream29.remenberingassistant

import kotlin.reflect.KProperty

val Throwable.recursiveMessage: String
    get() = buildString {
        var currentThrowable: Throwable? = this@recursiveMessage
        while (currentThrowable != null) {
            appendLine(currentThrowable.message ?: "unknown error")
            currentThrowable = currentThrowable.cause
        }
    }

data class ReloadableProperty<T>(val load: () -> T) {
    private var value: T = load()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    fun reload() {
        value = load()
    }
}

fun <T> reloadable(block: () -> T) = ReloadableProperty(block)