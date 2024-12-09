package com.trip.myapp.ui.community.detail

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.trip.myapp.domain.model.Archive
import com.trip.myapp.domain.model.Post
import com.trip.myapp.ui.map.write.MapWriteEvent

@Composable
fun CommunityDetailScreen(
    onBackClick: () -> Unit,
    viewModel: CommunityDetailViewModel = hiltViewModel()
) {
    val post by viewModel.post.collectAsStateWithLifecycle()
    val pagedArchives = viewModel.pagedArchives.collectAsLazyPagingItems()

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is CommunityDetailEvent.ScrapPost.Success -> {
                    showBottomSheet = false
                }

                is CommunityDetailEvent.ScrapPost.Failure -> {
                    Toast.makeText(context, "업로드 실패", Toast.LENGTH_SHORT).show()
                }

                is CommunityDetailEvent.ScrapPost.Loading -> {
                }
            }
        }
    }

    CommunityDetailScreen(
        onBackClick = onBackClick,
        post = post,
        isLoading = isLoading,
        archives = pagedArchives,
        scrapPost = viewModel::scrapPost,
        showBottomSheet = showBottomSheet,
        onShowBottomSheetChange = { showBottomSheet = it },
        postName = viewModel.postName ?: ""
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommunityDetailScreen(
    onBackClick: () -> Unit,
    post: Post,
    isLoading: Boolean,
    archives: LazyPagingItems<Archive>,
    scrapPost: (String) -> Unit,
    showBottomSheet: Boolean,
    onShowBottomSheetChange: (Boolean) -> Unit,
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
                    IconButton(onClick = {
                        onShowBottomSheetChange(true)
                    }) {
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
                ) {
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
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { onShowBottomSheetChange(false) },
            ) {
                LazyColumn() {
                    items(
                        count = archives.itemCount,
                        key = { index -> archives[index]?.id ?: index }
                    ) { index ->
                        val archive = archives[index]
                        if (archive != null) {
                            ListItem(
                                headlineContent = { Text(archive.name) },
                                trailingContent = {
                                    IconButton(
                                        onClick = {
                                            scrapPost(archive.id)
                                        }
                                    ) {
                                        Icon(
                                            Icons.Filled.Add,
                                            contentDescription = "Localized description",
                                        )
                                    }
                                })
                            HorizontalDivider ()

                        }
                    }
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostDetailContent(
    post: Post,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Slider(modifier = Modifier, post.imageUrlList)
        Spacer(modifier = Modifier.height(16.dp))
        ContentSection(post)
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
