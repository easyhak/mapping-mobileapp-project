package com.trip.myapp.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trip.myapp.R

data class NavigationItem(
    val icon: ImageVector,
    @StringRes val labelRes: Int,
    val onClick: () -> Unit,
    val isSelected: Boolean,
)

@Composable
fun HomeBottomNavigation(items: List<NavigationItem>) {
    BottomAppBar(
        tonalElevation = 10.dp,
        containerColor = Color.White,  // 배경 색상
        contentColor = MaterialTheme.colorScheme.primary   // 아이콘 및 텍스트 색상
    ) {

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(item.labelRes)) },
                onClick = item.onClick,
                selected = item.isSelected,
            )
        }
    }
}


sealed class HomeBottomNavItem(
    val icon: ImageVector,
    @StringRes val label: Int,
) {
    data object MyDream : HomeBottomNavItem(
        icon = Icons.Outlined.Group,
        label = R.string.icon_text_community,
    )

    data object Community : HomeBottomNavItem(
        icon = Icons.Outlined.LocationOn,
        label = R.string.icon_text_map,
    )

    data object Setting : HomeBottomNavItem(
        icon = Icons.Outlined.CollectionsBookmark,
        label = R.string.icon_text_archive,
    )
}