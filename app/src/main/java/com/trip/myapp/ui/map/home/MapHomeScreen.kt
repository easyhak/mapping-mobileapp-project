package com.trip.myapp.ui.map.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.trip.myapp.ui.HomeBottomNavItem
import com.trip.myapp.ui.HomeBottomNavigation
import com.trip.myapp.ui.NavigationItem
import com.trip.myapp.ui.theme.AppTheme
import java.time.YearMonth

@Composable
fun MapHomeScreen(
    onCommunityClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onWriteClick: () -> Unit,
    onDetailClick: () -> Unit,
    viewModel: MapHomeViewModel = hiltViewModel()
) {
    MapHomeScreen(
        onCommunityClick = onCommunityClick,
        onArchiveClick = onArchiveClick,
        onWriteClick = onWriteClick,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MapHomeScreen(
    onCommunityClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onWriteClick: () -> Unit
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
            // 지도에 마커 추가

        }
    }
}

@Composable
fun CalendarContent() {

    var currentMonth by rememberSaveable { mutableStateOf(YearMonth.now()) }
    val today = java.time.LocalDate.now() // 오늘 날짜

    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()
    val totalDays = lastDayOfMonth.dayOfMonth
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    val weekDays = listOf("일", "월", "화", "수", "목", "금", "토")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 저번 달
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Previous Month"
                )
            }

            // 현재 월 표시
            Text(
                text = "${currentMonth.year}년 ${currentMonth.monthValue}월",
                style = MaterialTheme.typography.titleLarge
            )

            // 다음 달
            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "Next Month"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 요일 헤더
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 요일 표시
            items(weekDays.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = weekDays[index],
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold
                        )
                    )
                }
            }

            // 빈 칸 처리 (월 시작 전 공백)
            items(firstDayOfWeek) {
                Box(modifier = Modifier.height(20.dp)) // 빈 칸
            }

            // 날짜 표시
            items(totalDays) { day ->
                val date = currentMonth.atDay(day + 1) // 현재 날짜 계산
                val isToday = date == today // 오늘 날짜인지 확인

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (day + 1).toString(),
                        style = if (isToday) {
                            MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold
                            )
                        } else {
                            MaterialTheme.typography.bodyMedium
                        }
                    )
                }
            }
        }
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
