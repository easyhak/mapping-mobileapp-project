package com.trip.myapp.ui.archive

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ArchiveRoute {
    const val ROUTE = "archive"
    const val NAME = "아카이브"
}

fun NavGraphBuilder.archiveScreen() {
    composable(route = ArchiveRoute.ROUTE) {
        ArchiveScreen()
    }
}
