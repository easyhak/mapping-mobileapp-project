package com.trip.myapp.ui

import android.util.Log
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    val items = listOf(
        BottomNavScreen.Community,
        BottomNavScreen.Map,
        BottomNavScreen.Archive,
    )

    BottomAppBar {
        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            IconButton(
                onClick = {
                    Log.d("navigation", screen.route)
                    navController.navigate(screen.route) {
                        popUpTo("home") {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = screen.icon,
                    contentDescription = screen.title,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
