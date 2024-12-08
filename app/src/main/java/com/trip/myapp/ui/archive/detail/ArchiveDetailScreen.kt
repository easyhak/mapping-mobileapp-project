package com.trip.myapp.ui.archive.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.trip.myapp.R
import com.trip.myapp.domain.model.Post
import com.trip.myapp.ui.theme.AppTheme
import com.trip.myapp.util.toLazyPagingItems

@Composable
fun ArchiveDetailScreen(
    onBackClick: () -> Unit,
    viewModel: ArchiveDetailViewModel = hiltViewModel()
) {
    val pagedPosts = viewModel.pagedPosts.collectAsLazyPagingItems()
    // 여러개의 post 가 있음

    ArchiveDetailScreen(
        archiveId = viewModel.archiveId,
        archiveName = viewModel.archiveName,
        onBackClick = onBackClick,
        posts = pagedPosts,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArchiveDetailScreen(
    archiveId: String?,
    archiveName: String?,
    onBackClick: () -> Unit,
    posts: LazyPagingItems<Post>
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
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
                    PostCardItem(
                        post = postItem,
                    )
                }
            }
        }
    }
}

@Composable
private fun PostCardItem(post: Post) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {}
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (post.imageUrlList.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://picsum.photos/200/300")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = post.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text("No Image", style = MaterialTheme.typography.bodyMedium)
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
            posts = dummyPosts
        )
    }
}

@Preview
@Composable
private fun PreviewArchiveDetailCard() {
    AppTheme {
        PostCardItem(
            post = Post(
                id = "1",
                title = "Title",
                content = "Content",
                imageUrlList = listOf("https://picsum.photos/200/300"),
                startDate = "",
                endDate = "",
                pinColor = 0,
                latitude = 0.0,
                longitude = 0.0,
                userId = "1"
            )
        )
    }
}
