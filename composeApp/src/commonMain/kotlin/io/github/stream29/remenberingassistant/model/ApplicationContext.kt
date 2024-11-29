package io.github.stream29.remenberingassistant.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import io.github.stream29.remenberingassistant.view.page.Page

data class ApplicationContext(
    val navHostController: NavHostController,
){
    var reloaded by mutableStateOf(false)
}

fun ApplicationContext.back() {
    navHostController.popBackStack()
}

fun ApplicationContext.navigate(route: Page) = navigate(route.toString())

fun ApplicationContext.navigate(route: String) =
    navHostController.navigate(route)