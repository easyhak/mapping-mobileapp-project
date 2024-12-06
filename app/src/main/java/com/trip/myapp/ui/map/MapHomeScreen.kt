package com.trip.myapp.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.trip.myapp.ui.HomeBottomNavItem
import com.trip.myapp.ui.HomeBottomNavigation
import com.trip.myapp.ui.NavigationItem
import com.trip.myapp.ui.map.component.BottomSheetAddContent
import com.trip.myapp.ui.theme.AppTheme

@Composable
fun MapHomeScreen(
    onCommunityClick: () -> Unit, onArchiveClick: () -> Unit, onDetailClick: () -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {

    val title = viewModel.title.collectAsStateWithLifecycle()
    val content = viewModel.content.collectAsStateWithLifecycle()
    val selectedImages = viewModel.selectedImages.collectAsStateWithLifecycle()
    val startDate = viewModel.startDate.collectAsStateWithLifecycle()
    val endDate = viewModel.endDate.collectAsStateWithLifecycle()
    val pinColor = viewModel.pinColor.collectAsStateWithLifecycle()
    val latitude = viewModel.latitude.collectAsStateWithLifecycle()
    val longitude = viewModel.longitude.collectAsStateWithLifecycle()
    val address = viewModel.address.collectAsStateWithLifecycle()

    MapHomeScreen(
        onCommunityClick = onCommunityClick,
        onArchiveClick = onArchiveClick,
        title = title.value,
        content = content.value,
        onTitleChange = viewModel::updateTitle,
        onContentChange = viewModel::updateContent,
        selectedImages = selectedImages.value,
        onImagesChange = viewModel::addSelectedImages,
        startDate = startDate.value,
        endDate = endDate.value,
        onStartDateChange = viewModel::updateStartDate,
        onEndDateChange = viewModel::updateEndDate,
        pinColor = pinColor.value,
        onPinColorChange = viewModel::updatePinColor,
        latitude = latitude.value,
        longitude = longitude.value,
        address = address.value,
        onLatitudeChange = viewModel::updateLatitude,
        onLongitudeChange = viewModel::updateLongitude,
        onAddressChange = viewModel::updateAddress,
        onSaveClick = viewModel::savePost

    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MapHomeScreen(
    onCommunityClick: () -> Unit,
    onArchiveClick: () -> Unit,
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    selectedImages: List<String>,
    onImagesChange: (List<String>) -> Unit,
    startDate: String?,
    endDate: String?,
    onStartDateChange: (String?) -> Unit,
    onEndDateChange: (String?) -> Unit,
    pinColor: Color,
    onPinColorChange: (Color) -> Unit,
    latitude: Double,
    longitude: Double,
    address: String,
    onLatitudeChange: (Double) -> Unit,
    onLongitudeChange: (Double) -> Unit,
    onAddressChange: (String) -> Unit,
    onSaveClick: () -> Unit

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

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // 부분적으로 확장되는 동작을 허용
    )

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
                onClick = { showBottomSheet = true },
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
                0 -> MapContent()
                1 -> CalendarContent()
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    modifier = Modifier.fillMaxHeight(), // 시트의 높이를 화면의 100%로 채움
                    sheetState = sheetState,
                    onDismissRequest = { showBottomSheet = false } // 시트를 닫을 때
                ) {
                    BottomSheetAddContent(
                        title = title,
                        content = content,
                        onTitleChange = onTitleChange,
                        onContentChange = onContentChange,
                        selectedImages = selectedImages,
                        onImagesChange = onImagesChange,
                        startDate = startDate,
                        endDate = endDate,
                        onStartDateChange = onStartDateChange,
                        onEndDateChange = onEndDateChange,
                        pinColor = pinColor,
                        onPinColorChange = onPinColorChange,
                        latitude = latitude,
                        longitude = longitude,
                        address = address,
                        onLatitudeChange = onLatitudeChange,
                        onLongitudeChange = onLongitudeChange,
                        onAddressChange = onAddressChange,
                        onSaveClick = onSaveClick
                    )
                }
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
        MapHomeScreen({}, {}, { })
    }
}
