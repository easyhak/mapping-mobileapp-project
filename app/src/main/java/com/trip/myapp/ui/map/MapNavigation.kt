package com.trip.myapp.ui.map

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.trip.myapp.ui.archive.ArchiveGraph
import com.trip.myapp.ui.community.CommunityGraph
import com.trip.myapp.ui.login.LoginRoute
import com.trip.myapp.ui.map.home.MapHomeScreen
import com.trip.myapp.ui.map.write.MapWriteScreen
import kotlinx.serialization.Serializable

@Serializable
data object MapGraph {
    @Serializable
    data object MapHomeRoute

    @Serializable
    data object MapWriteRoute

    @Serializable
    data class MapDetailRoute(val id: Long)
}

fun NavGraphBuilder.mapGraph(navController: NavController) {
    navigation<MapGraph>(
        startDestination = MapGraph.MapHomeRoute,
    ) {
        val options = NavOptions.Builder()
            .setPopUpTo(ArchiveGraph, inclusive = false, saveState = true)
            .setLaunchSingleTop(true)
            .build()
        composable<MapGraph.MapHomeRoute> {
            MapHomeScreen(
                onCommunityClick = {
                    navController.navigate(CommunityGraph, options)
                },
                onArchiveClick = {
                    navController.navigate(ArchiveGraph, options)
                },
                onWriteClick = {
                    navController.navigate(MapGraph.MapWriteRoute)
                },
                onDetailClick = { postId, postName ->
                    navController.navigate(CommunityGraph.CommunityDetailRoute)
                },
                onLogoutClick = {
                    val logOutOptions = NavOptions.Builder()
                        .setPopUpTo(MapGraph.MapHomeRoute, inclusive = true)
                        .setLaunchSingleTop(true)
                        .build()
                    navController.navigate(LoginRoute, navOptions = logOutOptions)
                }
            )
        }

        composable<MapGraph.MapWriteRoute> {
            MapWriteScreen(
                onBackClick = navController::navigateUp
            )
        }

    }
}
