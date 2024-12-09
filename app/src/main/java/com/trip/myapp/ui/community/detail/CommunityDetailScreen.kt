package com.trip.myapp.ui.community.detail

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
                    Toast.makeText(context, "스크랩 성공", Toast.LENGTH_SHORT).show()
                    
                }

                is CommunityDetailEvent.ScrapPost.Failure -> {
                    Toast.makeText(context, "스크랩 실패", Toast.LENGTH_SHORT).show()
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
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
                            HorizontalDivider()
                        }
                    }
                }
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
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Slider(images = post.imageUrlList)
            Spacer(modifier = Modifier.height(6.dp))
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
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)) {
        // 날짜 섹션
        SectionCard(label = "날짜") {
            Text(
                text = "${post.startDate} ~ ${post.endDate}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(modifier = Modifier.height(2.dp))

        // 내용 섹션
        SectionCard(label = "내용") {
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
        }
        Spacer(modifier = Modifier.height(2.dp))

        // 위치 섹션
        SectionCard(label = "위치") {
            Text(
                text = post.address,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(modifier = Modifier.height(2.dp))

        // 구글 맵 섹션
        GoogleMapView(
            latitude = post.latitude,
            longitude = post.longitude,
            pinColor = post.pinColor,
        )
    }
}

@Composable
private fun SectionCard(label: String, content: @Composable () -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondaryContainer),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
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

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
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
}
