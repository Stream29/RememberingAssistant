package io.github.stream29.remenberingassistant

import java.io.File
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

data class AutoSavableFileDelegate(val file: File) {
    init {
        if (!file.exists())
            file.createNewFile()
    }

    private var value: String = file.readText()
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = value
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        this.value = value
        file.writeText(value)
        println("$file Saved with content: $value")
    }

    fun reload() {
        value = file.readText()
    }
}