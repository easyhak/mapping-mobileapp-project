package com.trip.myapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreateNewFolder
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.trip.myapp.ui.archive.archiveScreen
import com.trip.myapp.ui.community.communityScreen
import com.trip.myapp.ui.login.LoginRoute
import com.trip.myapp.ui.login.loginScreen
import com.trip.myapp.ui.login.navigateToCommunityScreen
import com.trip.myapp.ui.map.mapScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val bottomNavScreens = listOf(
        BottomNavScreen.Community,
        BottomNavScreen.Map,
        BottomNavScreen.Archive
    )

    val bottomNavRoutes = bottomNavScreens.map { it.route }

    Scaffold(
        topBar = {
            if (currentRoute in bottomNavRoutes) {
                val currentScreen = bottomNavScreens.find { it.route == currentRoute }
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = currentScreen?.title ?: "")
                    }
                )
            }
        },
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomNavigationBar(navController = navController)
            }
        },
        floatingActionButton = {
            when(currentRoute){
                BottomNavScreen.Community.route -> {
                    FloatingActionButton(
                        onClick = {
                            navController.navigateToCommunityScreen()
                        }
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "글쓰기"
                            )
                        }
                    }
                }
                BottomNavScreen.Archive.route -> {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(BottomNavScreen.Community.route)
                        }
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CreateNewFolder,
                                contentDescription = "폴더 추가"
                            )
                        }
                    }
                }
            }

        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoginRoute.ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            loginScreen(
                onLoginSuccess = {
                    navController.navigateToCommunityScreen(
                        navOptions = navOptions {
                            popUpTo(LoginRoute.ROUTE) { inclusive = true }
                        }
                    )
                }
            )
            communityScreen()
            mapScreen()
            archiveScreen()
        }
    }
}
