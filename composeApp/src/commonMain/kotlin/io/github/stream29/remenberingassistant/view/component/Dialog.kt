package io.github.stream29.remenberingassistant.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.stream29.remenberingassistant.model.ApplicationContext

@Composable
fun ApplicationContext.FailDialog(message: String, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = true
        )
    ) {
        Column(
            modifier = Modifier.background(Color.Cyan, RoundedCornerShape(10.dp)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = message,
                onValueChange = {},
                readOnly = true
            )
            Button(
                onClick = onDismissRequest
            ) {
                Text("OK")
            }
        }
    }
}