package com.trip.myapp.ui.archive

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.trip.myapp.ui.community.CommunityGraph
import com.trip.myapp.ui.map.MapGraph
import kotlinx.serialization.Serializable

@Serializable
data object ArchiveGraph {
    @Serializable
    data object ArchiveHomeRoute

    @Serializable
    data class ArchiveDetailRoute(val id: Long)
}

fun NavGraphBuilder.archiveGraph(navController: NavController) {
    navigation<ArchiveGraph>(
        startDestination = ArchiveGraph.ArchiveHomeRoute
    ) {

        composable<ArchiveGraph.ArchiveHomeRoute> {
            val options = NavOptions.Builder()
                .setPopUpTo(ArchiveGraph, inclusive = false, saveState = true)
                .setLaunchSingleTop(true)
                .build()
            ArchiveScreen(
                onCommunityClick = {
                    navController.navigate(CommunityGraph, options)
                },
                onMapClick = {
                    navController.navigate(MapGraph, options)
                },
                onDetailClick = {})
        }
    }
}
