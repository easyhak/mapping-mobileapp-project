package com.trip.myapp.ui.map.home

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.trip.myapp.domain.model.Post
import com.trip.myapp.ui.HomeBottomNavItem
import com.trip.myapp.ui.HomeBottomNavigation
import com.trip.myapp.ui.NavigationItem
import com.trip.myapp.ui.theme.AppTheme

@Composable
fun MapHomeScreen(
    onCommunityClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onWriteClick: () -> Unit,
    onDetailClick: () -> Unit,
    viewModel: MapHomeViewModel = hiltViewModel()
) {
    val posts = viewModel.postList.collectAsStateWithLifecycle()
    MapHomeScreen(
        onCommunityClick = onCommunityClick,
        onArchiveClick = onArchiveClick,
        onWriteClick = onWriteClick,
        posts = posts.value,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MapHomeScreen(
    onCommunityClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onWriteClick: () -> Unit,
    posts: List<Post>,
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    var selectedTabIndex1 = selectedTabIndex
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

    Scaffold(
        bottomBar = {
            HomeBottomNavigation(items = navigationItems)
        },
        topBar = {
            TopAppBar(
                { Text("Mapping") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onWriteClick,
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },

        ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(
                selectedTabIndex = selectedTabIndex1
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex1 == index,
                        onClick = { selectedTabIndex1 = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex1) {
                0 -> MapContent(
                    posts = posts
                )
                1 -> CalendarContent()
            }
        }

    }
}


@Composable
private fun MapContent(
    posts: List<Post>
) {
    // 대한민국의 중심 좌표 (위도, 경도)
    val koreaCenter = LatLng(37.5665, 126.9780)  // 서울, 대한민국

    // 카메라 위치 상태를 설정 (줌 레벨 10)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(koreaCenter, 10f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            posts.forEach {
                val intColor = it.pinColor.toInt()
                val hsv = FloatArray(3)
                Color.colorToHSV(intColor, hsv)
                val hue = hsv[0]
                Marker(
                    state = MarkerState(position = LatLng(it.latitude, it.longitude)),
                    title = it.title,
                    snippet = it.content,
                    icon = BitmapDescriptorFactory.defaultMarker(hue),
                    draggable = true
                )
            }

        }
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
    AppTheme {
        MapHomeScreen(
            onCommunityClick = {},
            onArchiveClick = { },
            onWriteClick = { },
            onDetailClick = { }
        )
    }
}
