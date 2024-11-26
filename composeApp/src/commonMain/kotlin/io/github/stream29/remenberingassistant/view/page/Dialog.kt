package io.github.stream29.remenberingassistant.view.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.stream29.remenberingassistant.model.ApplicationContext
import io.github.stream29.remenberingassistant.model.navigate

@Composable
fun ApplicationContext.FailDialog(message: String, navigateTo: Page) {
    Dialog(
        onDismissRequest = { navigate(navigateTo) },
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
            Text(message)
            Button(
                onClick = { navigate(navigateTo) }
            ) {
                Text("OK")
            }
        }
    }
}