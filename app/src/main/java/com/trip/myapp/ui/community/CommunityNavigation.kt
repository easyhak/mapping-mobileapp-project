package com.trip.myapp.ui.community

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object CommunityGraph {
    @Serializable
    data object CommunityHomeRoute

    @Serializable
    data class CommunityDetailRoute(val id: Long)
}


fun NavGraphBuilder.communityNavGraph(navController: NavController) {
    navigation<CommunityGraph>(
        startDestination = CommunityGraph.CommunityHomeRoute,
    ) {
        composable<CommunityGraph.CommunityHomeRoute> {
            CommunityScreen(onDetailClick = {
                navController.navigate(CommunityGraph.CommunityDetailRoute(1))
            })
        }
        composable<CommunityGraph.CommunityDetailRoute> {
            CommunityDetailScreen( navController::navigateUp)
        }
    }
}
