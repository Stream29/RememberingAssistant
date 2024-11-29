package io.github.stream29.remenberingassistant.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.stream29.remenberingassistant.apiAuthConfigFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class FileEditViewModel(
    val file: File = apiAuthConfigFile,
) {
    var content by mutableStateOf("")

    init {
        CoroutineScope(Dispatchers.IO).launch {
            content = file.readText()
        }
    }

    fun saveAnd(block: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            file.writeText(content)
            println("Saved")
            block()
        }
    }
}