package com.trip.myapp.ui.archive

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trip.myapp.ui.HomeBottomNavItem
import com.trip.myapp.ui.HomeBottomNavigation
import com.trip.myapp.ui.NavigationItem

data class Folder(
    val name: String,
    val imageRes: Int?,
    val isDefault: Boolean = false // 기본 폴더 여부 ('전체' 폴더는 true)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveScreen(
    onCommunityClick: () -> Unit,
    onMapClick: () -> Unit,
    onDetailClick: (String) -> Unit,
    viewModel: ArchiveViewModel = viewModel()
) {
    val folders by viewModel.folders.collectAsState()
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
            items(folders.size) { index ->
                val folder = folders[index]
                CardItem(
                    title = folder.name,
                    imageRes = folder.imageRes,
                    modifier = Modifier
                )
            }
        }
    }

    if (showDialog) {
        AddFolderDialog(
            onDismiss = { showDialog = false },
            onAddFolder = { folderName ->
                viewModel.addFolder(folderName) // ViewModel을 통해 폴더 추가
                showDialog = false
            }
        )
    }
}

@Composable
fun CardItem(title: String, imageRes: Int?, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
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
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Text("No Image", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun AddFolderDialog(onDismiss: () -> Unit, onAddFolder: (String) -> Unit) {
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
            TextButton(onClick = { if (folderName.text.isNotBlank()) onAddFolder(folderName.text) }) {
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
    ArchiveScreen(
        onCommunityClick = {},
        onMapClick = {},
        onDetailClick = {}
    )
}