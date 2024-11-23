package io.github.stream29.remenberingassistant

import java.io.File

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val dataDirectory: File