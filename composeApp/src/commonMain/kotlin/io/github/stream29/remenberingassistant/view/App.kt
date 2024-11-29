package io.github.stream29.remenberingassistant.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.stream29.remenberingassistant.Global
import io.github.stream29.remenberingassistant.apiAuthConfigFile
import io.github.stream29.remenberingassistant.memoryFile
import io.github.stream29.remenberingassistant.model.ApplicationContext
import io.github.stream29.remenberingassistant.view.page.FileEditPage
import io.github.stream29.remenberingassistant.view.page.HelloPage
import io.github.stream29.remenberingassistant.view.page.Page
import io.github.stream29.remenberingassistant.view.page.SafeStreamChatPage
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navHostController = rememberNavController()
    val applicationContext = remember {
        ApplicationContext(navHostController = navHostController)
    }
    with(applicationContext) {
        NavHost(
            navController = navHostController,
            startDestination = Page.HelloPage.toString(),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            composable(route = Page.HelloPage.toString()) {
                HelloPage()
            }
            composable(route = Page.StreamChatPage.toString()) {
                SafeStreamChatPage()
            }
            composable(route = Page.ApiConfigEditPage.toString()) {
                FileEditPage(Global.apiAuthDelegate)
            }
            composable(route = Page.MemoryEditPage.toString()) {
                FileEditPage(Global.memoryDelegate)
            }
        }
    }
}

