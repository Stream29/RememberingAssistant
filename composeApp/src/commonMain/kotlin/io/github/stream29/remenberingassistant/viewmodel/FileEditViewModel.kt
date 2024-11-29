package io.github.stream29.remenberingassistant.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.github.stream29.remenberingassistant.AutoSavableFileDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FileEditViewModel(
    val fileDelegate: AutoSavableFileDelegate
) : ViewModel() {
    private var fileContent by fileDelegate
    val contentState = mutableStateOf(fileContent)
    var content by contentState

    init {
        println("file ${fileDelegate.file} loaded with content: $content")
    }

    fun save() {
        CoroutineScope(Dispatchers.IO).launch {
            fileContent = content
        }
    }
}