package com.trip.myapp.ui.map

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.trip.myapp.ui.BottomNavScreen
import kotlinx.serialization.Serializable

sealed class MapRoute(val route: String, val title: String) {
    data object MapHomeRoute : MapRoute("map_home", "맵")
    data object MapDetailRoute : MapRoute("map_detail", "상세보기")
}

fun NavGraphBuilder.mapNavGraph(navController: NavController) {
    navigation(
        startDestination = "map_main",
        route = BottomNavScreen.Map.route
    ) {
        composable("map_main") {
            MapScreen(onDetailClick = {
            })
        }
    }
}
