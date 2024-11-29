package io.github.stream29.remenberingassistant.view.page

sealed interface Page {
    data object HelloPage : Page
    data object StreamChatPage : Page
    data object FileEditPage : Page
}