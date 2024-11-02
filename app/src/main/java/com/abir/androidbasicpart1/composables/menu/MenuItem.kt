package com.abir.androidbasicpart1.composables.menu

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.abir.androidbasicpart1.data.MenuItemDetails

@Composable
fun MenuItem(
    index: Int,
    selectedItemIndex: Int,
    item: MenuItemDetails,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(text = item.title) },
        selected = index == selectedItemIndex,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = if (index == selectedItemIndex) {
                    item.selectedIcon
                } else item.unselectedIcon,
                contentDescription = item.title
            )
        },
        badge = {  // Show Badge
            item.badgeCount?.let {
                Text(text = item.badgeCount.toString())
            }
        },
        modifier = Modifier
            .padding(NavigationDrawerItemDefaults.ItemPadding) //padding between items
    )
}