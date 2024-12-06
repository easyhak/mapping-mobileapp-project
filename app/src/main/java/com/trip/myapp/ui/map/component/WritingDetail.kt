package com.trip.myapp.ui.map.component


import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.trip.myapp.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// ModalBottomSheet 안의 내용을 별도로 분리한 컴포저블 함수
@Composable
fun BottomSheetAddContent(
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
    onAddressChange: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // bottom sheet 제목
        item { ShowingTitle() }

        // 제목
        item {
            WritingTitle(
                title = title,
                onTitleChange = onTitleChange
            )
        }

        //내용
        item {
            WritingExplanation(
                content = content,
                onContentChange = onContentChange
            )
        }
        //사진
        item {
            PickingPhoto(
                selectedImages = selectedImages,
                onImagesChange = onImagesChange
            )
        }

        //날짜
        item {
            PickingDate(
                startDate = startDate,
                endDate = endDate,
                onStartDateChange = onStartDateChange,
                onEndDateChange = onEndDateChange
            )
        }

        //지도
        item {
            PickingLocation(
                pinColor = pinColor,
                onPinColorChange = onPinColorChange,
                longitude = longitude,
                latitude = latitude,
                address = address,
                onLatitudeChange = onLatitudeChange,
                onLongitudeChange = onLongitudeChange,
                onAddressChange = onAddressChange
            )
        }

        //버튼
        item {
            Button(
                onClick = {
                    /* Todo */
                },
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 20.dp)
                    .size(width = 250.dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.inversePrimary),
                enabled = true

            ) {
                Text(text = "기록 완료")
            }
        }
    }
}

@Composable
private fun ShowingTitle() {
    Text(
        text = "여행 기록하기",
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable

private fun WritingTitle(
    title: String,
    onTitleChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        placeholder = {
            Text("제목을 입력하세요")
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(60.dp),
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceDim,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceDim,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        )
    )
}

@Composable
private fun WritingExplanation(
    content: String,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = content,
        onValueChange = onContentChange,
        placeholder = {
            Text("내용을 입력하세요")
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .height(250.dp),
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults
            .colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceDim,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceDim,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            )
    )
}

@Composable
private fun PickingPhoto(
    selectedImages: List<String>,
    onImagesChange: (List<String>) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { uris ->
            val names = uris.mapNotNull { uri ->
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    if (nameIndex != -1) cursor.getString(nameIndex) else null
                }
            }
            // 선택된 이미지 목록 업데이트
            onImagesChange(names)
        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 아이콘 클릭 시 PhotoPicker 실행
        Icon(
            imageVector = Icons.Outlined.Image,
            contentDescription = "image",
            modifier = Modifier
                .padding(start = 16.dp)
                .size(48.dp)
                .clickable {
                    launcher.launch(arrayOf("image/*"))
                }
        )

        // 선택한 이미지 이름 표시
        Column(modifier = Modifier.padding(start = 8.dp)) {
            if (selectedImages.isNotEmpty()) {
                selectedImages.forEach { imageName ->
                    Text(
                        text = imageName,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            } else {
                Text(
                    text = "아이콘을 눌러 이미지를 선택해주세요",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun PickingDate(
    startDate: String?,
    endDate: String?,
    onStartDateChange: (String?) -> Unit,
    onEndDateChange: (String?) -> Unit
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 캘린더 아이콘
        Icon(
            imageVector = Icons.Filled.CalendarMonth,
            contentDescription = "calendar",
            modifier = Modifier
                .padding(start = 16.dp)
                .size(48.dp)
                .clickable { isDialogOpen = true }
        )

        Text(
            text = if (startDate != null && endDate != null && startDate != "" && endDate != "") {
                "$startDate ~ $endDate"
            } else {
                "아이콘을 눌러 시작날짜와 종료날짜를 \n선택해주세요"
            },
            style = MaterialTheme.typography.bodyMedium,
            //color = if (startDate != null && endDate != null) MaterialTheme.colorScheme.primary else Color.Gray,
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp)
        )
    }

    // DateRangePickerModal 표시
    if (isDialogOpen) {
        DateRangePickerModal(
            onDateRangeSelected = { range ->
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                // 이 부분 수정하기
                onStartDateChange(range.first?.let { formatter.format(Date(it)) })
                onEndDateChange(range.second?.let { formatter.format(Date(it)) })
                isDialogOpen = false
            },
            onDismiss = { isDialogOpen = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "당일치기 여행인 경우, 날짜를 두번 눌러 선택 해주세요~!"
                )
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
}

@Composable
private fun PickingLocation(
    pinColor: Color,
    onPinColorChange: (Color) -> Unit,
    longitude: Double,
    latitude: Double,
    address: String,
    onLongitudeChange: (Double) -> Unit,
    onLatitudeChange: (Double) -> Unit,
    onAddressChange: (String) -> Unit
) {
    var showColorDialog by remember { mutableStateOf(false) }
    var showMapDialog by remember { mutableStateOf(false) }


    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 15f) // 초기 카메라 위치 설정
    }

    // 비동기로 주소를 가져오는 함수

    fun fetchAddressFromCoordinates(lat: Double, lng: Double, onAddressFetched: (String) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val geocoder = Geocoder(context, Locale("ko", "KR"))
            geocoder.getFromLocation(lat, lng, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: List<Address>) {
                    val result = addresses.firstOrNull()?.getAddressLine(0)
                    onAddressFetched(result ?: "주소를 가져올 수 없습니다.")
                }

                override fun onError(errorMessage: String?) {
                    onAddressFetched("주소를 가져올 수 없습니다.")
                }
            })
        } else {
            val geocoder = Geocoder(context, Locale("ko", "KR"))
            val result = try {
                geocoder.getFromLocation(lat, lng, 1)?.firstOrNull()?.getAddressLine(0)
            } catch (e: Exception) {
                null
            }
            onAddressFetched(result ?: "주소를 가져올 수 없습니다.")
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 핀 아이콘
        Icon(
            imageVector = Icons.Filled.PushPin,
            contentDescription = "pin",
            modifier = Modifier
                .padding(start = 16.dp)
                .size(48.dp)
                .clickable {
                    showMapDialog = true // 지도를 표시하는 다이얼로그 열기
                },
            tint = pinColor
        )

        Column(modifier = Modifier.padding(start = 8.dp)) {
            // 핀 위치 설정 설명 텍스트
            Text(
                text = "핀 아이콘을 눌러 위치를 설정해보세요",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color.Gray
            )

            // 색상 변경 텍스트
            Text(
                text = "핀 색변경",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Blue),
                modifier = Modifier.clickable {
                    showColorDialog = true
                }
            )
        }
    }

    // 지도 다이얼로그
    if (showMapDialog) {
        MapDialog(
            initialLatitude = latitude, // 기존 뷰모델의 latitude 사용
            initialLongitude = longitude, // 기존 뷰모델의 longitude 사용
            onLocationSelected = { newLatitude, newLongitude ->
                onLatitudeChange(newLatitude)  // 뷰모델의 latitude 변경
                onLongitudeChange(newLongitude) // 뷰모델의 longitude 변경
                fetchAddressFromCoordinates(newLatitude, newLongitude) { newAddress ->
                    onAddressChange(newAddress)  // 뷰모델의 address 변경
                }
                cameraPositionState.position =
                    CameraPosition.fromLatLngZoom(
                        LatLng(newLatitude, newLongitude),
                        15f
                    ) // 카메라 상태 업데이트
                showMapDialog = false
            },
            onDismiss = { showMapDialog = false }
        )
    }

    // 색상 변경 다이얼로그
    if (showColorDialog) {
        ColorPickerDialog(
            onColorSelected = { selectedColor ->
                onPinColorChange(selectedColor)
                showColorDialog = false
            },
            onDismiss = { showColorDialog = false }
        )
    }

    // 작은 지도와 주소 표시
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = address,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            cameraPositionState = cameraPositionState, // 공통 카메라 상태 사용
            onMapClick = { latLng ->
                onLatitudeChange(latLng.latitude)  // 뷰모델의 latitude 변경
                onLongitudeChange(latLng.longitude) // 뷰모델의 longitude 변경
                fetchAddressFromCoordinates(latLng.latitude, latLng.longitude) { address ->
                    onAddressChange(address)  // 뷰모델의 address 변경
                }
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LatLng(latLng.latitude, latLng.longitude),
                    15f
                ) // 클릭 시 카메라 위치 갱신
            }
        ) {
            Marker(
                state = MarkerState(
                    position = LatLng(
                        latitude,
                        longitude
                    )
                ), // 뷰모델에서 관리하는 latitude, longitude 사용
                title = "선택한 위치"
            )
        }
    }
}

// 지도 다이얼로그
@Composable
fun MapDialog(
    initialLatitude: Double,
    initialLongitude: Double,
    onLocationSelected: (Double, Double) -> Unit,
    onDismiss: () -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(initialLatitude, initialLongitude), 15f)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("위치를 선택하세요") },
        text = {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    onLocationSelected(latLng.latitude, latLng.longitude)
                }
            )
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("확인")
            }
        }
    )
}
@Composable
fun ColorPickerDialog(
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    // 색상 선택 다이얼로그
    Column(
        modifier = Modifier
            .fillMaxWidth()

            .padding(16.dp)
    ) {
        Text("색을 선택하세요", style = MaterialTheme.typography.bodyLarge)

        // 색상 옵션들
        Row(modifier = Modifier.padding(top = 16.dp)) {
            val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Black)
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(4.dp)
                        .background(color = color, shape = MaterialTheme.shapes.medium)
                        .clickable {
                            onColorSelected(color)
                        }
                )
            }
        }

        // 취소 버튼
        TextButton(
            onClick = onDismiss,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("취소")
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewBottomSheet() {
    AppTheme {
        BottomSheetAddContent(
            title = "여행 기록",
            content = "우와",
            onTitleChange = {},
            onContentChange = {},
            selectedImages = emptyList(),
            onImagesChange = {},
            startDate = null,
            endDate = null,
            onStartDateChange = { },
            onEndDateChange = { },
            pinColor = Color.Black,
            onPinColorChange = {},
            latitude = 37.1,
            longitude = 120.1,
            onLatitudeChange = {},
            onLongitudeChange = {
            },
            onAddressChange = {},
            address = ""
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCalendar() {
    AppTheme {
        DateRangePickerModal(onDateRangeSelected = {}, onDismiss = {})
    }
}
