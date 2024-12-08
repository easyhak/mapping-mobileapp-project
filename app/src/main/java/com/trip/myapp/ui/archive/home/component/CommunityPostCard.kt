package com.trip.myapp.ui.archive.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.trip.myapp.domain.model.Post
import com.trip.myapp.ui.theme.AppTheme

@Composable
fun CommunityPostCard(
    post: Post,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
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
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp) // 약간의 패딩 추가
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PostCardPreview() {
    AppTheme {
        CommunityPostCard(
            Post(
                userName = "김민수",
                imageUrlList = listOf("https://picsum.photos/200/300")
            ),
        )
    }
}
