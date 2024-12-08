package com.trip.myapp.ui.community.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.trip.myapp.domain.model.Post
import com.trip.myapp.ui.HomeBottomNavItem
import com.trip.myapp.ui.HomeBottomNavigation
import com.trip.myapp.ui.NavigationItem
import com.trip.myapp.ui.archive.home.component.CommunityPostCard
import kotlinx.coroutines.flow.flowOf

@Composable
fun CommunityHomeScreen(
    onMapClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onDetailClick: (String, String) -> Unit,
    viewModel: CommunityHomeViewModel = hiltViewModel()
) {

    val posts = viewModel.pagedPosts.collectAsLazyPagingItems()
    CommunityHomeScreen(
        onMapClick = onMapClick,
        onArchiveClick = onArchiveClick,
        onDetailClick = onDetailClick,
        post = posts
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommunityHomeScreen(
    onMapClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onDetailClick: (String, String) -> Unit,
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),

            ) {
            items(
                count = post.itemCount,
                key = { index -> post[index]?.id ?: index }
            ) { index ->
                val postItem = post[index]
                if (postItem != null) {
                    CommunityPostCard(
                        post = postItem,
                        onDetailClick = onDetailClick
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
        onDetailClick = { _, _ -> },
        post = flowOf(PagingData.from(listOf(Post()))).collectAsLazyPagingItems()
    )
}
