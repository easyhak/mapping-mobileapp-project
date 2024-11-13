package com.trip.myapp.ui.community

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object CommunityRoute {
    const val ROUTE = "community"
    const val NAME = "커뮤니티"
}

//fun NavController.navigateToCommunityScreen(navOptions: NavOptions? = null) {
//    this.navigate(CommunityScreenRoute, navOptions)
//}

fun NavGraphBuilder.communityScreen() {
    composable(route = CommunityRoute.ROUTE) {
        CommunityScreen()
    }
}
