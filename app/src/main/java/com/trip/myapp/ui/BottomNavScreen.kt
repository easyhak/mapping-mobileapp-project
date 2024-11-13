package com.trip.myapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.trip.myapp.ui.archive.ArchiveRoute
import com.trip.myapp.ui.community.CommunityRoute
import com.trip.myapp.ui.map.MapRoute

sealed class BottomNavScreen(val route: String, val title: String, val icon: ImageVector) {
    data object Community : BottomNavScreen(CommunityRoute.ROUTE, CommunityRoute.NAME, Icons.Default.Person)
    data object Map : BottomNavScreen(MapRoute.ROUTE, MapRoute.NAME, Icons.Default.Home)
    data object Archive : BottomNavScreen(ArchiveRoute.ROUTE, ArchiveRoute.NAME, Icons.Default.Star)
}
