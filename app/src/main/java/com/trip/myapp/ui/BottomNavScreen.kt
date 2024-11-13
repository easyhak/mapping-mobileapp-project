package com.trip.myapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.ui.graphics.vector.ImageVector
import com.trip.myapp.ui.archive.ArchiveRoute
import com.trip.myapp.ui.community.CommunityRoute
import com.trip.myapp.ui.map.MapRoute

sealed class BottomNavScreen(val route: String, val title: String, val icon: ImageVector) {
    data object Community : BottomNavScreen(CommunityRoute.ROUTE, CommunityRoute.NAME, Icons.Filled.People)
    data object Map : BottomNavScreen(MapRoute.ROUTE, MapRoute.NAME, Icons.Filled.Map)
    data object Archive : BottomNavScreen(ArchiveRoute.ROUTE, ArchiveRoute.NAME, Icons.Filled.Archive)
}
