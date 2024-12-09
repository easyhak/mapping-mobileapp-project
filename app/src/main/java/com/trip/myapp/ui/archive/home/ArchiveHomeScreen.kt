package com.trip.myapp.ui.archive.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.trip.myapp.R
import com.trip.myapp.domain.model.Archive
import com.trip.myapp.ui.HomeBottomNavItem
import com.trip.myapp.ui.HomeBottomNavigation
import com.trip.myapp.ui.NavigationItem

@Composable
fun ArchiveHomeScreen(
    onCommunityClick: () -> Unit,
    onMapClick: () -> Unit,
    onDetailClick: (String, String) -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: ArchiveHomeViewModel = hiltViewModel()
) {
    val pagedArchives = viewModel.pagedArchives.collectAsLazyPagingItems()

    val loginEmail = viewModel.loginEmail
    var showEmailDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ArchiveHomeEvent.SignOUt.Success -> {
                    onLogoutClick()
                }

                is ArchiveHomeEvent.SignOUt.Failure -> {
                    Toast.makeText(context, "로그아웃 실패", Toast.LENGTH_SHORT).show()
                }

                else -> {

                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ArchiveHomeEvent.AddArchive.Success -> {
                    pagedArchives.refresh()
                }

                is ArchiveHomeEvent.AddArchive.Failure -> {
                    Toast.makeText(context, "생성 실패", Toast.LENGTH_SHORT).show()
                }

                else -> {

                }
            }
        }
    }
    ArchiveHomeScreen(
        onCommunityClick = onCommunityClick,
        onMapClick = onMapClick,
        onDetailClick = onDetailClick,
        archives = pagedArchives,
        addArchive = viewModel::addArchive,
        onSignOutClick = viewModel::signOut,
        onInfoClick = {
            // 내 정보 버튼 클릭 시 이메일 다이얼로그 표시
            showEmailDialog = true
        },
        loginEmail = loginEmail
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
fun ArchiveHomeScreen(
    onCommunityClick: () -> Unit,
    onMapClick: () -> Unit,
    onDetailClick: (String, String) -> Unit,
    archives: LazyPagingItems<Archive>,
    addArchive: (String) -> Unit,
    onSignOutClick: () -> Unit,
    onInfoClick: () -> Unit,
    loginEmail: String
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val navigationItems = listOf(
        NavigationItem(
            icon = HomeBottomNavItem.MyDream.icon,
            labelRes = HomeBottomNavItem.MyDream.label,
            isSelected = false,
            onClick = onCommunityClick,
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
            isSelected = true,
            onClick = {},
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
                    ) { // 버튼 클릭 시 메뉴 표시
                        Icon(
                            imageVector = Icons.Default.Person, // 프로필 아이콘 리소스
                            contentDescription = "Profile Icon"
                        )
                    }

                    // 간단히 호출
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(8.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
//            item {
//                ArchiveCardItem(
//                    archive = Archive(
//                        name = "전체"
//                    ),
//                    onDetailClick = onDetailClick,
//                )
//            }
            items(
                count = archives.itemCount,
                key = { index -> archives[index]?.id ?: index }
            ) { index ->
                val archive = archives[index]
                if (archive != null) {
                    ArchiveCardItem(
                        archive = archive,
                        onDetailClick = onDetailClick,
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddArchiveDialog(
            onDismiss = { showDialog = false },
            onAddFolder = addArchive
        )
    }
}

@Composable
fun ArchiveCardItem(
    archive: Archive,
    onDetailClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onDetailClick(archive.id, archive.name)
            }
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (archive.thumbnailImageUrl.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(archive.thumbnailImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = archive.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop,
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(archive.color)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ImageNotSupported,
                    contentDescription = "사진",
                    tint = Color.White
                )
            }
        }
        Text(
            text = archive.name,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),

            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun AddArchiveDialog(onDismiss: () -> Unit, onAddFolder: (String) -> Unit) {
    var folderName by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("새 폴더 이름") },
        text = {
            OutlinedTextField(
                value = folderName,
                onValueChange = { folderName = it },
                label = { Text("폴더 이름을 입력하세요") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = {
                if (folderName.text.isNotBlank()) {
                    onAddFolder(folderName.text)
                }
                onDismiss()
            }) {
                Text("추가")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("취소")
            }
        }
    )
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


@Preview(showBackground = true)
@Composable
private fun PreviewArchiveScreen() {
    ArchiveHomeScreen(
        onCommunityClick = {},
        onMapClick = {},
        onDetailClick = { _, _ ->
            {}
        },
        onLogoutClick = {}
    )
}
