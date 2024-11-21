package com.trip.myapp.ui.archive

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object ArchiveGraph {
    @Serializable
    data object ArchiveHomeRoute

    @Serializable
    data class ArchiveDetailRoute(val id: Long)
}

fun NavGraphBuilder.archiveGraph(navController: NavController) {
    navigation<ArchiveGraph.ArchiveHomeRoute>(
        startDestination = ArchiveGraph.ArchiveHomeRoute
    ) {
        composable<ArchiveGraph.ArchiveHomeRoute> {
            ArchiveScreen({})
        }
    }
}