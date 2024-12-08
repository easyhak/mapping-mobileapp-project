package com.trip.myapp.ui.community.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.trip.myapp.domain.model.Post
import com.trip.myapp.ui.HomeBottomNavItem
import com.trip.myapp.ui.HomeBottomNavigation
import com.trip.myapp.ui.NavigationItem
import com.trip.myapp.ui.archive.detail.PostCardItem
import com.trip.myapp.ui.archive.home.CardItem
import com.trip.myapp.ui.archive.home.component.CommunityPostCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun CommunityHomeScreen(
    onMapClick: () -> Unit, onArchiveClick: () -> Unit,
    viewModel: CommunityHomeViewModel = hiltViewModel()
) {

    val posts = viewModel.pagedPosts.collectAsLazyPagingItems()
    CommunityHomeScreen(
        onMapClick = onMapClick,
        onArchiveClick = onArchiveClick,
        post = posts
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommunityHomeScreen(
    onMapClick: () -> Unit,
    onArchiveClick: () -> Unit,
    post: LazyPagingItems<Post>
) {
    val navigationItems = listOf(
        NavigationItem(
            icon = HomeBottomNavItem.MyDream.icon,
            labelRes = HomeBottomNavItem.MyDream.label,
            isSelected = true,
            onClick = {},
        ),
        NavigationItem(
            icon = HomeBottomNavItem.Community.icon,
            labelRes = HomeBottomNavItem.Community.label,
            isSelected = false,
            onClick = onMapClick,
        ),
        NavigationItem(
            icon = HomeBottomNavItem.Setting.icon,
            labelRes = HomeBottomNavItem.Setting.label,
            isSelected = false,
            onClick = onArchiveClick,
        ),
    )
    Scaffold(
        bottomBar = {
            HomeBottomNavigation(items = navigationItems)
        },
        topBar = {
            TopAppBar(
                { Text("Mapping") },
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(
                count = post.itemCount,
                key = { index -> post[index]?.id ?: index }
            ) { index ->
                val postItem = post[index]
                if (postItem != null) {
                    CommunityPostCard(
                        post = postItem,
                    )
                }
            }
        }

    }
}


@Preview
@Composable
private fun CommunityScreenPreview() {
    CommunityHomeScreen(
        onArchiveClick = {},
        onMapClick = {},
        post = flowOf(PagingData.from(listOf(Post()))).collectAsLazyPagingItems()
    )
}
