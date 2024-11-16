package com.trip.myapp.ui.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute {
    const val ROUTE = "login"
    const val NAME = "로그인"
}

fun NavGraphBuilder.loginScreen(onLoginSuccess: () -> Unit) {
    composable(route = LoginRoute.ROUTE) {
        LoginScreen(
            onLoginSuccess = onLoginSuccess
        )
    }
}
