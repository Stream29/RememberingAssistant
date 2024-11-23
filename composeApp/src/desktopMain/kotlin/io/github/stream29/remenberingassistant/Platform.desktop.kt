package io.github.stream29.remenberingassistant

import java.io.File

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual val dataDirectory: File
    get() = File("./")