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
    data object Community : BottomNavScreen("community_graph", "커뮤니티", Icons.Default.People)
    data object Map : BottomNavScreen("map_graph", "지도", Icons.Default.Map)
    data object Archive : BottomNavScreen("archive_graph", "아카이브", Icons.Default.Archive) // route를 "archive_graph"로 변경
}
