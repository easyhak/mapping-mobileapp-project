package com.trip.myapp.ui.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

fun NavGraphBuilder.loginScreen(navController: NavController) {
    composable<LoginRoute> {
        LoginScreen({})
    }
}
