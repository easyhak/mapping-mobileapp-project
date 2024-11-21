package com.trip.myapp.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.trip.myapp.ui.HomeBottomNavItem
import com.trip.myapp.ui.HomeBottomNavigation
import com.trip.myapp.ui.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(onCommunityClick: ()-> Unit, onArchiveClick: ()-> Unit, onDetailClick: () -> Unit) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val navigationItems = listOf(
        NavigationItem(
            icon = HomeBottomNavItem.MyDream.icon,
            labelRes = HomeBottomNavItem.MyDream.label,
            isSelected = false,
            onClick = onCommunityClick,
        ),
        NavigationItem(
            icon = HomeBottomNavItem.Community.icon,
            labelRes = HomeBottomNavItem.Community.label,
            isSelected = true,
            onClick = {},
        ),
        NavigationItem(
            icon = HomeBottomNavItem.Setting.icon,
            labelRes = HomeBottomNavItem.Setting.label,
            isSelected = false,
            onClick = onArchiveClick,
        ),
    )

    val tabs = listOf("Map", "Calendar")

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
        Column (modifier = Modifier.padding(innerPadding)){
            TabRow (
                selectedTabIndex = selectedTabIndex
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> MapContent()
                1 -> CalendarContent()
            }
        }

    }
}


@Composable
fun MapContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Text("Map Content", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun CalendarContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Text("Calendar Content", style = MaterialTheme.typography.titleLarge)
    }
}


@Preview
@Composable
private fun PreviewMapScreen() {
    MapScreen ({}, {}, {  })
}