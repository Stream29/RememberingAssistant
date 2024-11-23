package io.github.stream29.remenberingassistant.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.stream29.remenberingassistant.viewmodel.ContextViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val contextViewModel = remember { ContextViewModel() }
        val textRecord by contextViewModel.stateFlow.collectAsState()
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                    contextViewModel.chat("Button clicked")
                    println("on click")
                }
            ) {
                Text("Click me!")
            }
            Text(textRecord)
        }
    }
}