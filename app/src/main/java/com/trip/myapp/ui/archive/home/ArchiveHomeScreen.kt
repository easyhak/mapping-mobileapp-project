package com.trip.myapp.ui.archive.home

import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.trip.myapp.domain.model.Archive
import com.trip.myapp.ui.HomeBottomNavItem
import com.trip.myapp.ui.HomeBottomNavigation
import com.trip.myapp.ui.NavigationItem

@Composable
fun ArchiveHomeScreen(
    onCommunityClick: () -> Unit,
    onMapClick: () -> Unit,
    onDetailClick: (String) -> Unit,
    viewModel: ArchiveHomeViewModel = hiltViewModel()
) {
    val pagedArchives = viewModel.pagedArchives.collectAsLazyPagingItems()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ArchiveHomeEvent.AddArchive.Success -> {
                    pagedArchives.refresh()
                }

                is ArchiveHomeEvent.AddArchive.Failure -> {
                    Toast.makeText(context, "생성 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    ArchiveHomeScreen(
        onCommunityClick = onCommunityClick,
        onMapClick = onMapClick,
        onDetailClick = onDetailClick,
        archives = pagedArchives,
        addArchive = viewModel::addArchive
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveHomeScreen(
    onCommunityClick: () -> Unit,
    onMapClick: () -> Unit,
    onDetailClick: (String) -> Unit,
    archives: LazyPagingItems<Archive>,
    addArchive: (String) -> Unit
) {

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
                { Text("Mapping") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
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
            items(
                count = archives.itemCount,
                key = { index -> archives[index]?.id ?: index }
            ) { index ->
                val archive = archives[index]
                if (archive != null) {
                    CardItem(
                        archive = archive,
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
fun CardItem(archive: Archive, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { }
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (archive.thumbnailImageUrl.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(archive.thumbnailImageUrl.isNotEmpty())
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
                Text("No Image", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Text(
            text = archive.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
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

@Preview(showBackground = true)
@Composable
private fun PreviewArchiveScreen() {
    ArchiveHomeScreen(
        onCommunityClick = {},
        onMapClick = {},
        onDetailClick = {}
    )
}