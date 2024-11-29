package io.github.stream29.remenberingassistant

import java.io.File

expect val dataDirectory: File

val apiAuthConfigFile = dataDirectory.resolve("ApiAuth.yml")

val memoryFile = dataDirectory.resolve("memory.txt")

val currentApiAuthFile = dataDirectory.resolve("currentApiAuth.txt")