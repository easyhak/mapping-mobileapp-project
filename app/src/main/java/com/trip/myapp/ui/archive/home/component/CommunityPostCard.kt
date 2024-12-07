package com.trip.myapp.ui.archive.home.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.trip.myapp.R
import com.trip.myapp.domain.model.Post
import com.trip.myapp.ui.theme.AppTheme

@Composable
fun CommunityPostCard(
    post: Post,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            /* todo coil 로 변경 */
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(post.userProfileImageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = post.title,
                modifier = Modifier
                    .width(40.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.size(8.dp))
            Column {
                Text(text = post.userName, style = MaterialTheme.typography.bodySmall)
                Text(text = post.address, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "more vert"
                )
            }
        }
        Log.d("CommunityPostCard", "post.imageUrlList.first() = $post")
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(post.imageUrlList.first())
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = post.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PostCardPreview() {
    AppTheme {
        CommunityPostCard(
            Post(
                userName = "김민수",
                imageUrlList = listOf("https://picsum.photos/200/300")),
        )
    }
}
