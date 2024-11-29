package io.github.stream29.remenberingassistant

import java.io.File

internal var _dataDirectory: File = File("/emulated")
actual val dataDirectory: File
    get() = _dataDirectory