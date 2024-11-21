package com.trip.myapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.trip.myapp.ui.archive.archiveGraph
import com.trip.myapp.ui.community.CommunityGraph
import com.trip.myapp.ui.community.communityGraph
import com.trip.myapp.ui.login.LoginRoute
import com.trip.myapp.ui.login.loginScreen
import com.trip.myapp.ui.map.mapGraph

@Composable
fun MainScreen(
    onGoogleSignInClick: () -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute, // LoginRoute
    ) {
        // 로그인 화면
        loginScreen(navController = navController)

        communityGraph(navController = navController)
        mapGraph(navController = navController)
        archiveGraph(navController = navController)

    }
}
