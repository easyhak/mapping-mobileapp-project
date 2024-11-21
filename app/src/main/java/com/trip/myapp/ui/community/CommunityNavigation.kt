package com.trip.myapp.ui.community

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.trip.myapp.ui.archive.ArchiveGraph
import com.trip.myapp.ui.map.MapGraph
import kotlinx.serialization.Serializable

@Serializable
data object CommunityGraph {
    @Serializable
    data object CommunityHomeRoute

    @Serializable
    data class CommunityDetailRoute(val id: Long)
}


fun NavGraphBuilder.communityGraph(navController: NavController) {
    navigation<CommunityGraph>(
        startDestination = CommunityGraph.CommunityHomeRoute,
    ) {
        composable<CommunityGraph.CommunityHomeRoute> {
            val options = NavOptions.Builder()
                .setPopUpTo(CommunityGraph, inclusive = false, saveState = true)
                .setLaunchSingleTop(true)
                .build()
            CommunityScreen(
                onArchiveClick = {
                    navController.navigate(ArchiveGraph, options)
                },
                onMapClick = {
                    navController.navigate(MapGraph, options)
                }
            )
        }
        composable<CommunityGraph.CommunityDetailRoute> {
            CommunityDetailScreen( navController::navigateUp)
        }
    }
}
