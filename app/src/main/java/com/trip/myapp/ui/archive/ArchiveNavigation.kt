package com.trip.myapp.ui.archive

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.trip.myapp.ui.BottomNavScreen


sealed class ArchiveRoute(val route: String, val title: String) {
    data object ArchiveHomeRoute : ArchiveRoute("archive_home", "아카이브")
    data object ArchiveDetailRoute : ArchiveRoute("archive_detail", "상세보기")
}


fun NavGraphBuilder.archiveScreen(navController: NavHostController) {
    navigation(
        startDestination = ArchiveRoute.ArchiveHomeRoute.route,
        route = "archive_graph" // todo: 변수로 정의해야함
    ) {
        composable(route = ArchiveRoute.ArchiveHomeRoute.route) {
            ArchiveScreen(
                onNavigateToDetail = {
                    // navController.navigate("${ArchiveRoute.ArchiveDetailRoute.route}/$it")
                }
            )
        }

        composable(
            route = "${ArchiveRoute.ArchiveDetailRoute.route}/{archiveId}",
            arguments = listOf(navArgument("archiveId") { type = NavType.StringType })
        ) { backStackEntry ->
            val archiveId = backStackEntry.arguments?.getString("archiveId") ?: return@composable
            ArchiveDetailScreen(archiveId = archiveId)
        }
    }
}
