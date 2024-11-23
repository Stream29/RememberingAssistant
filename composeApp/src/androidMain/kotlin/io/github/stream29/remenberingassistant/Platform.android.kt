package io.github.stream29.remenberingassistant

import android.os.Build
import java.io.File

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

internal var _dataDirectory: File = File("/emulated")
actual val dataDirectory: File
    get() = _dataDirectory