package com.trip.myapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.trip.myapp.ui.archive.archiveScreen
import com.trip.myapp.ui.community.communityScreen
import com.trip.myapp.ui.login.LoginRoute
import com.trip.myapp.ui.login.loginScreen
import com.trip.myapp.ui.map.mapScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val bottomNavScreens = listOf(
        BottomNavScreen.Community,
        BottomNavScreen.Map,
        BottomNavScreen.Archive
    )

    Scaffold(
        topBar = {
            TopBar(
                currentDestination = currentDestination,
                bottomNavScreens = bottomNavScreens,
                navController = navController
            )
        },
        bottomBar = {
            if (currentDestination?.hierarchy?.any { it.route in bottomNavScreens.map { it.route } } == true) {
                BottomNavigationBar(navController = navController)
            }
        },
        floatingActionButton = {
            FloatingActionButtonForRoute(currentDestination, navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoginRoute.ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            loginScreen(
                onLoginSuccess = {
                    navController.navigate(
                        route = BottomNavScreen.Community.route,
                        navOptions = navOptions {
                            popUpTo(LoginRoute.ROUTE) { inclusive = true }
                        }
                    )
                }
            )
            communityScreen(navController = navController)
            mapScreen()
            archiveScreen(navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    currentDestination: NavDestination?,
    bottomNavScreens: List<BottomNavScreen>,
    navController: NavController
) {
    val canNavigateBack = navController.previousBackStackEntry != null

    val currentScreen = bottomNavScreens.find { screen ->
        currentDestination?.hierarchy?.any { it.route == screen.route } == true
    }

    if (currentScreen != null || canNavigateBack) {
        CenterAlignedTopAppBar(
            title = {
                Text(text = currentScreen?.title ?: "")
            },
            navigationIcon = {
                if (canNavigateBack) {

                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                }
            }
        )
    }
}

@Composable
fun FloatingActionButtonForRoute(
    currentDestination: NavDestination?,
    navController: NavController
) {
    when {
        currentDestination?.hierarchy?.any { it.route == BottomNavScreen.Community.route } == true -> {
            FloatingActionButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "글쓰기"
                )
            }
        }
        currentDestination?.hierarchy?.any { it.route == BottomNavScreen.Archive.route } == true -> {
            FloatingActionButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Outlined.CreateNewFolder,
                    contentDescription = "폴더 추가"
                )
            }
        }
    }
}
