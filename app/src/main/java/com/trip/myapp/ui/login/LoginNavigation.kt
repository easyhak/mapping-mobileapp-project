package com.trip.myapp.ui.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.trip.myapp.ui.map.MapGraph
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

fun NavGraphBuilder.loginScreen(navController: NavController) {
    composable<LoginRoute> {
        LoginScreen(
            onClickLoginButton = {
                val options = NavOptions.Builder()
                    .setPopUpTo(LoginRoute, inclusive = true)
                    .setLaunchSingleTop(true)
                    .build()
                navController.navigate(MapGraph, options)
            }
        )
    }
}
