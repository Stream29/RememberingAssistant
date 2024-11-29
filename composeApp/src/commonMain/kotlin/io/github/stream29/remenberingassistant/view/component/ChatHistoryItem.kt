package io.github.stream29.remenberingassistant.view.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Divider
import androidx.compose.material.Text
import com.mikepenz.markdown.m2.Markdown

fun LazyListScope.ChatHistoryItem(
    content: String
) = item {
    if (content.contains("\n"))
        Markdown(content)
    else
        Text(content)
    Divider()
}