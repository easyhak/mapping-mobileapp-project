package com.trip.myapp.ui.map.home


import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.trip.myapp.R
import com.trip.myapp.domain.model.Post
import com.trip.myapp.ui.HomeBottomNavItem
import com.trip.myapp.ui.HomeBottomNavigation
import com.trip.myapp.ui.NavigationItem
import com.trip.myapp.ui.theme.AppTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun MapHomeScreen(
    onCommunityClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onWriteClick: () -> Unit,
    onDetailClick: (String, String) -> Unit,
    onLogoutClick: () -> Unit,

    viewModel: MapHomeViewModel = hiltViewModel()
) {
    val posts = viewModel.postList.collectAsStateWithLifecycle()
    val loginEmail = viewModel.loginEmail
    var showEmailDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is MapHomeEvent.SignOUt.Success -> {
                    onLogoutClick()
                }

                is MapHomeEvent.SignOUt.Failure -> {
                    Toast.makeText(context, "로그아웃 실패", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    MapHomeScreen(
        onCommunityClick = onCommunityClick,
        onArchiveClick = onArchiveClick,
        onWriteClick = onWriteClick,
        onSignOutClick = viewModel::signOut,
        onDetailClick = onDetailClick,
        posts = posts.value,
        onInfoClick = {
            // 내 정보 버튼 클릭 시 이메일 다이얼로그 표시
            showEmailDialog = true
        },
        loginEmail = loginEmail
    )
    if (showEmailDialog) {
        AlertDialog(
            onDismissRequest = { showEmailDialog = false },
            title = { Text(text = "내 정보") },
            text = { Text(text = "이메일: $loginEmail") },
            confirmButton = {
                TextButton(onClick = { showEmailDialog = false }) {
                    Text("확인")
                }
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MapHomeScreen(
    onCommunityClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onWriteClick: () -> Unit,
    onDetailClick: (String, String) -> Unit,
    onSignOutClick: () -> Unit,
    posts: List<Post>,
    onInfoClick: () -> Unit,
    loginEmail: String
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var showMenu by remember { mutableStateOf(false) } // 메뉴 표시 상태 추가

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
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_mapping),
                        contentDescription = "Logo Image"
                    )
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) { // 버튼 클릭 시 메뉴 표시
                        Icon(
                            imageVector = Icons.Default.Person, // 프로필 아이콘 리소스
                            contentDescription = "Profile Icon"
                        )
                    }

                    // 간단히 호출
                    ProfileDropdownMenuWrapper(
                        showMenu = showMenu,
                        onDismissRequest = { showMenu = false },
                        onInfoClick = {
                            showMenu = false
                            println("내 정보 눌림")
                            onInfoClick()// TODO: 내정보 처리 추가

                        },
                        onLogoutClick = {
                            showMenu = false
                            onSignOutClick()

                        }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onWriteClick,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },

        ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
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
                0 -> MapContent(
                    posts = posts
                )

                1 -> CalendarContent(
                    // todo 월 별로 가져오기
                    posts = posts,
                    onDetailClick = onDetailClick
                )
            }
        }

    }
}


@Composable
private fun ProfileDropdownMenuWrapper(
    showMenu: Boolean,
    onDismissRequest: () -> Unit,
    onInfoClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    ProfileDropdownMenu(
        showMenu = showMenu,
        onDismissRequest = onDismissRequest,
        onInfoClick = onInfoClick,
        onLogoutClick = onLogoutClick
    )
}

@Composable
private fun ProfileDropdownMenu(
    showMenu: Boolean,
    onDismissRequest: () -> Unit,
    onInfoClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = onDismissRequest // 메뉴 외부를 클릭하면 닫힘
    ) {
        DropdownMenuItem(
            text = { Text("내 정보") },
            onClick = onInfoClick

        )
        DropdownMenuItem(
            text = { Text("로그아웃") },
            onClick = onLogoutClick
        )
    }
}

@Composable
private fun MapContent(
    posts: List<Post>,
    modifier: Modifier = Modifier
) {
    // 대한민국의 중심 좌표 (위도, 경도)
    val koreaCenter = LatLng(37.5665, 126.9780)  // 서울, 대한민국

    // 카메라 위치 상태를 설정 (줌 레벨 10)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(koreaCenter, 10f)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,  // 확대/축소 버튼 비 활성화
                compassEnabled = false,       // 나침반 비 활성화
                mapToolbarEnabled = false    // 지도 툴바 비 활성화
            )
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
private fun CalendarContent(
    posts: List<Post>,
    onDetailClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {

    var currentMonth by rememberSaveable { mutableStateOf(YearMonth.now()) }
    var selectedPosts by remember { mutableStateOf<List<Post>>(emptyList()) }
    val today = LocalDate.now()

    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()
    val totalDays = lastDayOfMonth.dayOfMonth
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    val weekDays = listOf("일", "월", "화", "수", "목", "금", "토")

    Column(
        modifier = modifier
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
            // 이전 달
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
                    imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = "Next Month"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 요일 헤더
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
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
                            fontWeight = FontWeight.ExtraBold
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
                val date = currentMonth.atDay(day + 1)
                val isToday = date == today

                // 해당 날짜에 표시할 Post 찾기
                val postsForDate = posts.filter { post ->
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val start = LocalDate.parse(post.startDate, formatter)
                    val end = LocalDate.parse(post.endDate, formatter)
                    date in start..end
                }

                Box(
                    modifier = Modifier
                        .clickable {
                            selectedPosts = postsForDate // 선택된 날짜의 Post 업데이트
                        }
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (day + 1).toString(),
                        style = if (isToday) {
                            MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.ExtraBold
                            )
                        } else {
                            MaterialTheme.typography.bodyMedium
                        }
                    )

                    // 하이라이트 색상 추가
                    if (postsForDate.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(8.dp)
                                .background(
                                    androidx.compose.ui.graphics.Color(postsForDate.first().pinColor),
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 선택된 날짜의 Post 정보 표시
        Column(modifier = Modifier.fillMaxWidth()) {
            selectedPosts.forEach { post ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDetailClick(post.id, post.title) }
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.PushPin,
                        contentDescription = "Pin",
                        tint = androidx.compose.ui.graphics.Color(post.pinColor),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.bodyLarge
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
            onDetailClick = { _, _ -> },
            onLogoutClick = { }
        )
    }
}
