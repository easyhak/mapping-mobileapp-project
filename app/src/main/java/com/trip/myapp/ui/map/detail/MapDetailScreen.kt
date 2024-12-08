package com.trip.myapp.ui.map.detail

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.trip.myapp.R
import com.trip.myapp.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapDetailScreen() {


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer // AppBar 배경색 설정
                ),
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "여행 제목",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* 뒤로가기 동작 */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* 편집하기 동작 */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Comment,
                            contentDescription = "Edit",
                            tint = Color.White
                        )
                    }
                }
            )
        },
    )
    { innerPadding ->
        Column(modifier = Modifier) {
            Slider(modifier = Modifier.padding(innerPadding))
            Spacer(modifier = Modifier.height(16.dp))
            ContentSection()
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Slider(modifier: Modifier = Modifier) {
    data class CarouselItem(
        val id: Int,
        @DrawableRes val imageResId: Int,
        @StringRes val contentDescriptionResId: Int
    )

    val items =
        listOf(
            CarouselItem(0, R.drawable.category_sample_1, R.string.app_name),
            CarouselItem(1, R.drawable.category_sample_1, R.string.app_name),
            CarouselItem(2, R.drawable.category_sample_1, R.string.app_name),

            )

    HorizontalUncontainedCarousel(
        state = rememberCarouselState { items.count() },
        modifier = modifier
            .width(412.dp)
            .height(221.dp),
        itemWidth = 186.dp,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { i ->
        val item = items[i]
        Image(
            modifier = Modifier
                .height(205.dp)
                .maskClip(MaterialTheme.shapes.extraLarge),
            painter = painterResource(id = item.imageResId),
            contentDescription = stringResource(item.contentDescriptionResId),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ContentSection() {
    val address = "대한민국 서울특별시 동작구 흑석로 84"
    val latitude = 37.50477221482922
    val longitude = 126.95648934692144

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(start = 30.dp, top = 16.dp)) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Calendar",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "날짜1 ~ 날짜2",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.padding(start = 30.dp, end = 30.dp)) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Comment,
                contentDescription = "Calendar",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua...",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 아이콘과 주소
        Row(modifier = Modifier.padding(start = 30.dp, top = 16.dp)) {
            Icon(
                imageVector = Icons.Filled.PushPin,
                contentDescription = "PushPin",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = address,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 구글 맵
        GoogleMapView(latitude = latitude, longitude = longitude)
    }
}

@Composable
fun GoogleMapView(latitude: Double, longitude: Double) {
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
            LatLng(latitude, longitude), 15f
        )
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 30.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(
                position = LatLng(
                    latitude,
                    longitude
                )
            ),
            title = "선택한 위치"
        )
    }
}

@Preview
@Composable
private fun PreviewMapScreen() {
    AppTheme {
        MapDetailScreen()
    }
}
