package io.github.stream29.remenberingassistant.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ContextViewModel : ViewModel() {
    val stateFlow = MutableStateFlow("")
    fun chat(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            stateFlow.value += message + "\n"
            stateFlow.value += "starting request...\n"
            delay(1000)
            stateFlow.value += "request completed\n"
        }
    }
}