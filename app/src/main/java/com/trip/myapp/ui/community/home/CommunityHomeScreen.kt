package com.trip.myapp.ui.community.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.trip.myapp.R
import com.trip.myapp.domain.model.Post
import com.trip.myapp.ui.HomeBottomNavItem
import com.trip.myapp.ui.HomeBottomNavigation
import com.trip.myapp.ui.NavigationItem
import com.trip.myapp.ui.archive.home.component.CommunityPostCard

@Composable
fun CommunityHomeScreen(
    onMapClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDetailClick: (String, String) -> Unit,
    viewModel: CommunityHomeViewModel = hiltViewModel()
) {
    val posts = viewModel.pagedPosts.collectAsLazyPagingItems()
    val loginEmail = viewModel.loginEmail
    var showEmailDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is CommunityHomeEvent.SignOUt.Success -> {
                    onLogoutClick()
                }

                is CommunityHomeEvent.SignOUt.Failure -> {
                    Toast.makeText(context, "로그아웃 실패", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    CommunityHomeScreen(
        onMapClick = onMapClick,
        onArchiveClick = onArchiveClick,
        onSignOutClick = viewModel::signOut,
        onInfoClick = {
            // 내 정보 버튼 클릭 시 이메일 다이얼로그 표시
            showEmailDialog = true
        },
        loginEmail = loginEmail,
        onDetailClick = onDetailClick,
        post = posts
    )
    if (showEmailDialog) {
        AlertDialog(
            onDismissRequest = { showEmailDialog = false },
            title = { Text(text = "내 정보") },
            text = { Text(text = "이메일: $loginEmail") },
            confirmButton = {
                TextButton(onClick = { showEmailDialog = false }) {
                    Text("확인")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommunityHomeScreen(
    onMapClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onInfoClick: () -> Unit,
    loginEmail: String,
    onDetailClick: (String, String) -> Unit,
    post: LazyPagingItems<Post>
) {
    var showMenu by remember { mutableStateOf(false) }

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
                modifier = Modifier.padding(bottom = 8.dp),
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_mapping),
                        contentDescription = "Logo Image",
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier
                            .padding(top = 20.dp, end = 16.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Icon"
                        )
                    }
                    ProfileDropdownMenuWrapper(
                        showMenu = showMenu,
                        onDismissRequest = { showMenu = false },
                        onInfoClick = {
                            showMenu = false
                            onInfoClick()
                        },
                        onLogoutClick = {
                            showMenu = false
                            onSignOutClick()
                        }
                    )
                }
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

@Composable
private fun ProfileDropdownMenuWrapper(
    showMenu: Boolean,
    onDismissRequest: () -> Unit,
    onInfoClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    ProfileDropdownMenu(
        showMenu = showMenu,
        onDismissRequest = onDismissRequest,
        onInfoClick = onInfoClick,
        onLogoutClick = onLogoutClick
    )
}

@Composable
fun ProfileDropdownMenu(
    showMenu: Boolean,
    onDismissRequest: () -> Unit,
    onInfoClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = onDismissRequest // 메뉴 외부를 클릭하면 닫힘
    ) {
        DropdownMenuItem(
            text = { Text("내 정보") },
            onClick = onInfoClick

        )
        DropdownMenuItem(
            text = { Text("로그아웃") },
            onClick = onLogoutClick
        )
    }
}

@Preview
@Composable
private fun CommunityScreenPreview() {
    CommunityHomeScreen(
        onArchiveClick = {},
        onMapClick = {},
        onLogoutClick = {},
        onDetailClick = { _, _ -> },
        //post = flowOf(PagingData.from(listOf(Post()))).collectAsLazyPagingItems()
    )
}
