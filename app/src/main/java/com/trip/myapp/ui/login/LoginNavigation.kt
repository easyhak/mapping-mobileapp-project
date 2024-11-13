package com.trip.myapp.ui.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.trip.myapp.ui.community.CommunityRoute
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute {
    const val ROUTE = "login"
    const val NAME = "로그인"
}

fun NavController.navigateToCommunityScreen(navOptions: NavOptions? = null) = navigate(route = CommunityRoute.ROUTE, navOptions = navOptions)

fun NavGraphBuilder.loginScreen(onLoginSuccess: () -> Unit) {
    composable(route = LoginRoute.ROUTE) {
        LoginScreen(
            onLoginSuccess = onLoginSuccess
        )
    }
}
