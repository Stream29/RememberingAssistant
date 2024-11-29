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

data class ReloadablePropertyBase<T>(
    val load: () -> T
) {
    private var _value: T = load()
    val value: T
        get() = _value

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    fun reload() {
        _value = load()
    }
}

fun <T> reloadable(block: () -> T) = ReloadablePropertyBase(block)