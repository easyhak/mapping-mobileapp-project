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
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.trip.myapp.ui.archive.ArchiveScreen
import com.trip.myapp.ui.community.CommunityScreen
import com.trip.myapp.ui.login.LoginRoute
import com.trip.myapp.ui.login.loginScreen
import com.trip.myapp.ui.map.MapScreen

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
            // 로그인 화면
            loginScreen(
                onLoginSuccess = {
                    navController.navigate(
                        route = "home",
                        navOptions = navOptions {
                            popUpTo(LoginRoute.ROUTE) { inclusive = true }
                        }
                    )
                }
            )

            // 홈 화면
            navigation(
                startDestination = BottomNavScreen.Community.route,
                route = "home"
            ) {
                // 커뮤니티 그래프
                navigation(
                    startDestination = "community_main",
                    route = BottomNavScreen.Community.route
                ) {
                    composable("community_main") { CommunityScreen({}) }
                    // composable("friend_list") { FriendListScreen() }
                    // composable("add_friend") { AddFriendScreen() }
                    // composable("post_detail") { PostDetailScreen() }
                }

                // 맵 그래프
                navigation(
                    startDestination = "map_main",
                    route = BottomNavScreen.Map.route
                ) {
                    composable("map_main") { MapScreen({}) }
                    // composable("map_detail") { MapDetailScreen() }
                }

                // 아카이브 그래프
                navigation(
                    startDestination = "archive_main",
                    route = BottomNavScreen.Archive.route
                ) {
                    composable("archive_main") { ArchiveScreen({}) }
                    // composable("archive_detail") { ArchiveDetailScreen() }
                    // composable("add_archive") { AddArchiveScreen() }
                }
            }
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
