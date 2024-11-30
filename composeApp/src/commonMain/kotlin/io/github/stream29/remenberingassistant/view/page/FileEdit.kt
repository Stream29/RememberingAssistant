package io.github.stream29.remenberingassistant.view.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.stream29.remenberingassistant.AutoSavableFileDelegate
import io.github.stream29.remenberingassistant.model.ApplicationContext
import io.github.stream29.remenberingassistant.model.back
import io.github.stream29.remenberingassistant.model.navigate
import io.github.stream29.remenberingassistant.viewmodel.FileEditViewModel

@Composable
fun ApplicationContext.FileEditPage(fileDelegate: AutoSavableFileDelegate) = MaterialTheme {
    val viewModel = remember { FileEditViewModel(fileDelegate) }
    with(viewModel) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(modifier = Modifier.fillMaxWidth().weight(0.1f)) {
                Button(onClick = { back() }) { Text("<") }
                Text(text = "${fileDelegate.file.path}", modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        save()
                        navigate(Page.HelloPage)
                    }
                ) { Text("Save") }
            }
            TextField(
                value = content.also { println("compose with content: $it") },
                onValueChange = { content = it;println("updated content: $content") },
                modifier = Modifier.fillMaxWidth().weight(0.9f)
            )
        }
    }
}