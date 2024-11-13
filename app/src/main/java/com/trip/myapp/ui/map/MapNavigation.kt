package com.trip.myapp.ui.map

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object MapRoute {
    const val ROUTE = "map"
    const val NAME = "지도"
}

fun NavGraphBuilder.mapScreen() {
    composable(route = MapRoute.ROUTE) {
        MapScreen()
    }
}
