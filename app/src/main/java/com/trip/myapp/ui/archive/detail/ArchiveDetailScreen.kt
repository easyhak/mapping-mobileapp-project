package com.trip.myapp.ui.archive.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.trip.myapp.domain.model.Post
import com.trip.myapp.ui.theme.AppTheme
import com.trip.myapp.util.toLazyPagingItems

@Composable
fun ArchiveDetailScreen(
    onBackClick: () -> Unit,
    onDetailClick: (String, String) -> Unit,
    viewModel: ArchiveDetailViewModel = hiltViewModel()
) {
    val pagedPosts = viewModel.pagedPosts.collectAsLazyPagingItems()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ArchiveDetailEvent.UnscrapPost.Success -> {
                    pagedPosts.refresh()
                }

                is ArchiveDetailEvent.UnscrapPost.Failure -> {
                    Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ArchiveDetailScreen(
        archiveId = viewModel.archiveId,
        archiveName = viewModel.archiveName,
        onBackClick = onBackClick,
        onDetailClick = onDetailClick,
        onUnscrapPost = viewModel::unscrapPost,
        posts = pagedPosts,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArchiveDetailScreen(
    archiveId: String?,
    archiveName: String?,
    onBackClick: () -> Unit,
    onDetailClick: (String, String) -> Unit,
    onUnscrapPost: (String) -> Unit,
    posts: LazyPagingItems<Post>
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer // AppBar 배경색 설정
                ),
                title = {
                    Text(archiveName ?: "찾을 수 없습니다.")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                count = posts.itemCount,
                key = { index -> posts[index]?.id ?: index }
            ) { index ->
                val postItem = posts[index]
                if (postItem != null) {
                    ArchiveDetailPostCard(
                        post = postItem,
                        onDetailClick = onDetailClick,
                        onUnscrapPost = onUnscrapPost,
                    )
                }
            }
        }
    }
}


@Composable
private fun ArchiveDetailPostCard(
    post: Post,
    onDetailClick: (String, String) -> Unit,
    onUnscrapPost: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isLongPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        isLongPressed = true
                    },
                    onTap = {
                        if (!isLongPressed) {
                            onDetailClick(post.id, post.title)
                        }
                    }
                )
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(post.imageUrlList.first())
                .crossfade(true)
                .build(),
            contentDescription = "image",
            modifier = Modifier
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
        )
        if (post.imageUrlList.size > 1) {
            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = "Multiple Photos Icon",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )
        }

        // Long click 상태에서 삭제 아이콘 표시
        if (isLongPressed) {
            IconButton(
                onClick = {
                    onUnscrapPost(post.id) // 삭제 클릭 처리
                    isLongPressed = false // 상태 초기화
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Icon",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewArchiveDetailScreen() {
    AppTheme {
        val dummyPosts = (0..20).map {
            Post(
                id = it.toString(),
                title = "Title",
                content = "Content",
                imageUrlList = emptyList(),
                startDate = "",
                endDate = "",
                pinColor = 0,
                latitude = 0.0,
                longitude = 0.0,
                userId = "1"
            )
        }.toLazyPagingItems()
        ArchiveDetailScreen(
            archiveId = "",
            archiveName = "테스트",
            onBackClick = {},
            posts = dummyPosts,
            onUnscrapPost = {},
            onDetailClick = { _, _ -> }
        )
    }
}
