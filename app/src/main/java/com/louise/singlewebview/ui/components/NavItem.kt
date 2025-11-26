package com.louise.singlewebview.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    var label: String,
    var icon: ImageVector,
    var badgeCount: Int
)

@Composable
fun getAllNavItem(): List<NavItem> {
    return listOf<NavItem>(
        NavItem("/", Icons.Default.Home, 0),
        NavItem("/notifications", Icons.Default.Call, 0),
        NavItem("/properties", Icons.Default.Share, 0),
        NavItem("/maintenance", Icons.Default.Notifications, 5),
        NavItem("/profile", Icons.Default.Settings, 0),
    )
}
