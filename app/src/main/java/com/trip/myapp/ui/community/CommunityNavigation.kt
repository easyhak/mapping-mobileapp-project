package com.trip.myapp.ui.community

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation


sealed class CommunityRoute(val route: String, val title: String) {
    data object CommunityHomeRoute : CommunityRoute("community_home", "커뮤니티")
    data object CommunityDetailRoute : CommunityRoute("community_detail", "상세보기")
}

fun NavGraphBuilder.communityScreen(navController: NavHostController) {

    navigation(
        startDestination = CommunityRoute.CommunityHomeRoute.route,
        route = "community_graph" // todo: 변수로 정의해야함
    ) {
        composable(route = CommunityRoute.CommunityHomeRoute.route) {
            CommunityScreen(
                onNavigateToDetail = {
                    // navController.navigate("${CommunityRoute.CommunityDetailRoute.route}/$it")
                }
            )
        }

        composable(
            route = "${CommunityRoute.CommunityDetailRoute.route}/{communityId}",
            arguments = listOf(navArgument("communityId") { type = NavType.StringType })
        ) { backStackEntry ->
            val communityId = backStackEntry.arguments?.getString("communityId") ?: return@composable
            CommunityDetailScreen(communityId = communityId)
        }
    }

}
