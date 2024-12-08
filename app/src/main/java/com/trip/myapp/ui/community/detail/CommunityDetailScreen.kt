package com.trip.myapp.ui.community.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.trip.myapp.domain.model.Post

@Composable
fun CommunityDetailScreen(
    onBackClick: () -> Unit,
    viewModel: CommunityDetailViewModel = hiltViewModel()
) {
    val post by viewModel.post.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    CommunityDetailScreen(
        onBackClick = onBackClick,
        post = post,
        isLoading = isLoading,
        postName = viewModel.postName ?: ""
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommunityDetailScreen(
    onBackClick: () -> Unit,
    post: Post,
    isLoading: Boolean,
    postName: String,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(postName)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.AutoMirrored.Filled.MenuOpen, contentDescription = "scrap")
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }
            else -> {
                PostDetailContent(
                    post = post,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostDetailContent(
    post: Post,
    modifier: Modifier = Modifier
) {


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
                            post.title,
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
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Slider(modifier = Modifier.padding(innerPadding), post.imageUrlList)
            Spacer(modifier = Modifier.height(16.dp))
            ContentSection(post)
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Slider(modifier: Modifier = Modifier, images: List<String>) {


    HorizontalUncontainedCarousel(
        state = rememberCarouselState { images.count() },
        modifier = modifier,
        itemWidth = 300.dp,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) { i ->
        val item = images[i]
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item)
                .crossfade(true)
                .build(),
            contentDescription = "image",
            modifier = Modifier
                .size(300.dp)
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun ContentSection(post: Post) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(start = 8.dp, top = 8.dp)) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Calendar",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${post.startDate} ~ ${post.endDate}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Comment,
                contentDescription = "Content",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 아이콘과 주소
        Row(modifier = Modifier.padding(start = 8.dp, top = 8.dp)) {
            Icon(
                imageVector = Icons.Filled.PushPin,
                contentDescription = "PushPin",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = post.address,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 구글 맵
        GoogleMapView(
            latitude = post.latitude,
            longitude = post.longitude,
            pinColor = post.pinColor
        )
    }
}

@Composable
fun GoogleMapView(
    latitude: Double,
    longitude: Double,
    pinColor: Long,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
            LatLng(latitude, longitude), 15f
        )
    }

    GoogleMap(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(horizontal = 8.dp),
        cameraPositionState = cameraPositionState
    ) {
        val intColor = pinColor.toInt()
        val hsv = FloatArray(3)
        android.graphics.Color.colorToHSV(intColor, hsv)
        val hue = hsv[0]
        Marker(
            state = MarkerState(
                position = LatLng(
                    latitude,
                    longitude
                )
            ),
            icon = BitmapDescriptorFactory.defaultMarker(hue),
        )
    }
}
