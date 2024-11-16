package com.trip.myapp.ui.community

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.trip.myapp.ui.BottomNavScreen


sealed class CommunityRoute(val route: String, val title: String) {
    data object CommunityHomeRoute : CommunityRoute("community_home", "커뮤니티")
    data object CommunityDetailRoute : CommunityRoute("community_detail", "상세보기")
}

fun NavGraphBuilder.communityNavGraph(navController: NavController) {
    navigation(
        startDestination = "community_main",
        route = BottomNavScreen.Community.route
    ) {
        composable("community_main") {
            CommunityScreen(onDetailClick = {
                navController.navigate("community_detail")
            })
        }
        composable("community_detail") {
            CommunityDetailScreen("")
        }
    }
}
