package com.abir.androidbasicpart1.datalayer.staticData

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

// Create Menu Items Class to Select Unselect items
data class MenuItemDetails(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

///List of Navigation Items that will be clicked
val menuItems = listOf(
    MenuItemDetails(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    MenuItemDetails(
        title = "API Integration",
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Outlined.Build
    ),
    MenuItemDetails(
        title = "Navigate Out of App",
        selectedIcon = Icons.Filled.Edit,
        unselectedIcon = Icons.Outlined.Edit,
        badgeCount = 105
    ),
    MenuItemDetails(
        title = "Firebase",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    ),
    MenuItemDetails(
        title = "Data Storage",
        selectedIcon = Icons.AutoMirrored.Filled.List,
        unselectedIcon = Icons.AutoMirrored.Outlined.List
    ),
    MenuItemDetails(
        title = "Ads",
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info
    ),
    MenuItemDetails(
        title = "Notification",
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.Notifications
    )
)
