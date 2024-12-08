package io.github.stream29.remenberingassistant

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant
import kotlin.reflect.KProperty
import kotlin.time.measureTime

val Throwable.recursiveMessage: String
    get() = buildString {
        var currentThrowable: Throwable? = this@recursiveMessage
        while (currentThrowable != null) {
            appendLine(currentThrowable.message ?: "unknown error")
            currentThrowable = currentThrowable.cause
        }
    }

data class AutoReloadableDelegate<T>(
    val load: () -> T
) {
    @Volatile
    private var timeStamp = Global.reload

    private var _value: T = load()
    val value: T
        get() = _value

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (timeStamp != Global.reload) {
            reload()
            timeStamp = Global.reload
        }
        return value
    }

    fun reload() {
        _value = load()
    }
}

data class AutoSavableFileDelegate(
    val file: File
) {
    init {
        if (!file.exists())
            file.createNewFile()
    }

    private var value: String = file.readText()
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
//        println("$file read with content: $value")
        return value
    }
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: String) {
        value = newValue
        CoroutineScope(Dispatchers.IO).launch {
            file.writeText(newValue)
            Global.reload = Instant.now()
            println("$file saved with content: $newValue")
        }
    }

    fun reload() {
        value = file.readText()
    }
}

inline fun <T : Any> timeScaled(block: () -> T): T {
    var result: T?
    val duration = measureTime { result = block() }
    println(duration)
    return result!!
}