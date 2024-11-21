package com.trip.myapp.ui.map

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object MapGraph {
    @Serializable
    data object MapHomeRoute

    @Serializable
    data class MapDetailRoute(val id: Long)
}
fun NavGraphBuilder.mapGraph(navController: NavController) {
    navigation<MapGraph>(
        startDestination = MapGraph.MapHomeRoute,
    ) {
        composable<MapGraph.MapHomeRoute> {
            MapScreen(onDetailClick = {
            })
        }

    }
}
