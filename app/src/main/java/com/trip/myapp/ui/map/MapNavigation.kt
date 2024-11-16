package com.trip.myapp.ui.map

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

sealed class MapRoute(val route: String, val title: String) {
    data object MapHomeRoute : MapRoute("map_home", "맵")
    data object MapDetailRoute : MapRoute("map_detail", "상세보기")
}

fun NavGraphBuilder.mapScreen() {
    composable(route = MapRoute.MapHomeRoute.route) {
        MapScreen({})
    }
}
