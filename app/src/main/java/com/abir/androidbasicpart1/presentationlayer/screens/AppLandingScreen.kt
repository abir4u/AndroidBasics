package com.abir.androidbasicpart1.presentationlayer.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.abir.androidbasicpart1.presentationlayer.composables.menu.MenuItem
import com.abir.androidbasicpart1.presentationlayer.composables.menu.MenuTopBar
import com.abir.androidbasicpart1.datalayer.staticData.menuItems
import com.abir.androidbasicpart1.presentationlayer.screens.ads.AdsHomeScreen
import com.abir.androidbasicpart1.presentationlayer.screens.api.ApiIntegrationScreen
import com.abir.androidbasicpart1.presentationlayer.screens.firebase.FirebaseScreen
import com.abir.androidbasicpart1.presentationlayer.screens.home.HomeScreen
import com.abir.androidbasicpart1.presentationlayer.screens.outsideapp.NavigateOutsideAppScreen
import com.abir.androidbasicpart1.presentationlayer.screens.storage.DataStorageScreen
import kotlinx.coroutines.launch

@Composable
fun AppLandingScreen() {
    //Remember Clicked index state and the Route Screen
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    var route by remember {  mutableStateOf("home") }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                menuItems.forEachIndexed { index, item ->
                    MenuItem(
                        index = index,
                        selectedItemIndex = selectedItemIndex,
                        item = item
                    ) {
                        selectedItemIndex = index
                        scope.launch {
                            drawerState.close()
                        }
                        when (selectedItemIndex) {
                            0 -> route = "home"
                            1 -> route = "api"
                            2 -> route = "outside"
                            3 -> route = "firebase"
                            4 -> route = "storage"
                            5 -> route = "ads"
                        }
                    }
                }
            }
        },
        gesturesEnabled = true
    ) {
        MenuTopBar {
            scope.launch {
                drawerState.apply {
                    if (isClosed) open() else close()
                }
            }
        }

        when (route) {
            "home" -> HomeScreen()
            "api" -> ApiIntegrationScreen()
            "outside" -> NavigateOutsideAppScreen()
            "firebase" -> FirebaseScreen()
            "storage" -> DataStorageScreen()
            "ads" -> AdsHomeScreen()
        }
    }
}