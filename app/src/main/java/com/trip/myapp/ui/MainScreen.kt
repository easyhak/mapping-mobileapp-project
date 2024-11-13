package com.trip.myapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.trip.myapp.ui.archive.archiveScreen
import com.trip.myapp.ui.community.communityScreen
import com.trip.myapp.ui.login.LoginRoute
import com.trip.myapp.ui.login.loginScreen
import com.trip.myapp.ui.login.navigateToCommunityScreen
import com.trip.myapp.ui.map.mapScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != LoginRoute.ROUTE) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoginRoute.ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            loginScreen(
                onLoginSuccess = navController::navigateToCommunityScreen
            )
            communityScreen()
            mapScreen()
            archiveScreen()
        }
    }
}
