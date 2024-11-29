package io.github.stream29.remenberingassistant.view.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.stream29.remenberingassistant.Global
import io.github.stream29.remenberingassistant.model.ApplicationContext
import io.github.stream29.remenberingassistant.model.navigate
import io.github.stream29.remenberingassistant.viewmodel.FileEditViewModel

@Composable
fun ApplicationContext.FileEditPage() = MaterialTheme {
    val viewModel = remember { FileEditViewModel() }
    with(viewModel) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(modifier = Modifier.fillMaxWidth().weight(0.1f)) {
                Text(text = "${file.path}", modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        saveAnd {
                            Global.apiProvidersProperty.reload()
                        }
                        reloaded = true
                        navigate(Page.HelloPage)
                    }
                ) { Text("Save") }
            }
            TextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier.fillMaxWidth().weight(0.9f)
            )
        }
    }
}