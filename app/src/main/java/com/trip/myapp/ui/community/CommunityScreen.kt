package com.trip.myapp.ui.community

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.trip.myapp.ui.HomeBottomNavItem
import com.trip.myapp.ui.HomeBottomNavigation
import com.trip.myapp.ui.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(onMapClick: ()->Unit, onArchiveClick: () -> Unit) {
    val navigationItems = listOf(
        NavigationItem(
            icon = HomeBottomNavItem.MyDream.icon,
            labelRes = HomeBottomNavItem.MyDream.label,
            isSelected = true,
            onClick = {},
        ),
        NavigationItem(
            icon = HomeBottomNavItem.Community.icon,
            labelRes = HomeBottomNavItem.Community.label,
            isSelected = false,
            onClick = onMapClick,
        ),
        NavigationItem(
            icon = HomeBottomNavItem.Setting.icon,
            labelRes = HomeBottomNavItem.Setting.label,
            isSelected = false,
            onClick = onArchiveClick,
        ),
    )

    Scaffold (
        bottomBar = {
            HomeBottomNavigation(items = navigationItems)
        },
        topBar = {
            TopAppBar(
                { Text("Mapping") },
            )
        }
    ) { innerPadding ->
        Text("커뮤니티 화면", modifier = Modifier.padding(innerPadding))

    }
}
