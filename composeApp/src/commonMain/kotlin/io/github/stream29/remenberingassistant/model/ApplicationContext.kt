package io.github.stream29.remenberingassistant.model

import androidx.navigation.NavHostController
import io.github.stream29.remenberingassistant.view.page.Page

data class ApplicationContext(
    val navHostController: NavHostController
)

fun ApplicationContext.navigate(route: Page) = navigate(route.toString())

fun ApplicationContext.navigate(route: String) =
    navHostController.navigate(route)