package io.github.stream29.remenberingassistant.view.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Divider
import androidx.compose.material.Text
import com.mikepenz.markdown.m2.Markdown

fun LazyListScope.ChatHistoryItem(
    sender: String,
    content: String
) = item {
    SelectionContainer {
        if (content.contains("\n"))
            Markdown(content)
        else
            Text(content)
    }
    Text("$sender: ")
    Divider()
}