package com.trip.myapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.ui.graphics.vector.ImageVector
import com.trip.myapp.ui.archive.ArchiveRoute
import com.trip.myapp.ui.community.CommunityRoute
import com.trip.myapp.ui.map.MapRoute

sealed class BottomNavScreen(val route: String, val homeRoute: String, val title: String, val icon: ImageVector) {
    object Community : BottomNavScreen("community_graph", "community_main", "커뮤니티", Icons.Default.People)
    object Map : BottomNavScreen("map_graph", "map_main", "지도", Icons.Default.Map)
    object Archive : BottomNavScreen("archive_graph", "archive_main", "아카이브", Icons.Default.Archive)
}
