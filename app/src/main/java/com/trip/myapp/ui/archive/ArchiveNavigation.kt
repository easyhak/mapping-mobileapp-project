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


fun NavGraphBuilder.archiveNavGraph(navController: NavController) {
    navigation(
        startDestination = "archive_main",
        route = BottomNavScreen.Archive.route
    ) {
        composable("archive_main") {
            ArchiveScreen(onDetailClick = {
                // 상세 화면으로 이동 로직 추가
            })
        }
    }
}