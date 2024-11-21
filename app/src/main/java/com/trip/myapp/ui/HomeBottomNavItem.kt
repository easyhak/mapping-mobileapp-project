package com.trip.myapp.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.trip.myapp.R

data class NavigationItem(
    val icon: ImageVector,
    @StringRes val labelRes: Int,
    val onClick: () -> Unit,
    val isSelected: Boolean,
)

@Composable
fun HomeBottomNavigation(items: List<NavigationItem>) {
    BottomAppBar {
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
        icon = Icons.Outlined.Hotel,
        label = R.string.icon_text_community,
    )

    data object Community : HomeBottomNavItem(
        icon = Icons.AutoMirrored.Outlined.Comment,
        label = R.string.icon_text_map,
    )

    data object Setting : HomeBottomNavItem(
        icon = Icons.Outlined.Settings,
        label = R.string.icon_text_archive,
    )
}