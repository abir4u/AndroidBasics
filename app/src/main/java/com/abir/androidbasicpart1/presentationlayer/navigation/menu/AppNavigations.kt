package com.abir.androidbasicpart1.presentationlayer.navigation.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abir.androidbasicpart1.businesslayer.viewmodels.storage.WalletViewModel
import com.abir.androidbasicpart1.presentationlayer.composables.storage.WalletList
import com.abir.androidbasicpart1.presentationlayer.screens.firebase.LoginSuccessScreen
import com.abir.androidbasicpart1.presentationlayer.screens.firebase.authentication.FirebaseHomeScreen
import com.abir.androidbasicpart1.presentationlayer.screens.firebase.authentication.FirebaseLoginHomeScreen
import com.abir.androidbasicpart1.presentationlayer.screens.firebase.authentication.email.FirebaseEmailLoginScreen
import com.abir.androidbasicpart1.presentationlayer.screens.firebase.authentication.email.FirebaseEmailRegisterScreen
import com.abir.androidbasicpart1.presentationlayer.screens.firebase.authentication.FirebaseRegisterHomeScreen
import com.abir.androidbasicpart1.presentationlayer.screens.firebase.authentication.google.FirebaseGoogleLoginScreen
import com.abir.androidbasicpart1.presentationlayer.screens.firebase.authentication.google.FirebaseGoogleRegisterScreen
import com.abir.androidbasicpart1.presentationlayer.screens.firebase.authentication.phone.FirebasePhoneLoginScreen
import com.abir.androidbasicpart1.presentationlayer.screens.firebase.authentication.phone.FirebasePhoneRegisterScreen
import com.abir.androidbasicpart1.presentationlayer.screens.storage.DataStorageScreen
import com.abir.androidbasicpart1.presentationlayer.screens.storage.roomdb.AddWalletItemScreen
import com.abir.androidbasicpart1.presentationlayer.screens.storage.roomdb.WalletDetailsScreen
import com.abir.androidbasicpart1.presentationlayer.screens.storage.roomdb.WalletHomeScreen

sealed class Screen(val route: String) {
    data object FirebaseHome : Screen("firebaseHome")
    data object LoginHome : Screen("loginHome")
    data object RegisterHome : Screen("registerHome")
    data object EmailLogin : Screen("emailLogin")
    data object EmailRegister : Screen("emailRegister")
    data object PhoneLogin : Screen("phoneLogin")
    data object PhoneRegister : Screen("phoneRegister")
    data object GoogleLogin : Screen("googleLogin")
    data object GoogleRegister : Screen("googleRegister")
    data object LoginSuccess : Screen("loginSuccess")
    data object WalletHome : Screen("wallet_home")
    data object WalletList : Screen("wallet_list")
    data object AddWalletItem : Screen("add_wallet_item")
    data object WalletDetails : Screen("wallet_detail/{itemId}")
    data object StorageHome : Screen("storageHome")
    data object SharedPreference : Screen("sharedPref")
    data object RoomDb : Screen("roomDb")
}

@Composable
fun FirebaseScreensStack() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.FirebaseHome.route) {
        composable(Screen.FirebaseHome.route) { FirebaseHomeScreen(navController) }
        composable(Screen.LoginHome.route) { FirebaseLoginHomeScreen(navController) }
        composable(Screen.RegisterHome.route) { FirebaseRegisterHomeScreen(navController) }
        composable(Screen.EmailLogin.route) { FirebaseEmailLoginScreen(navController) }
        composable(Screen.EmailRegister.route) { FirebaseEmailRegisterScreen(navController) }
        composable(Screen.PhoneLogin.route) { FirebasePhoneLoginScreen(navController) }
        composable(Screen.PhoneRegister.route) { FirebasePhoneRegisterScreen(navController) }
        composable(Screen.GoogleLogin.route) { FirebaseGoogleLoginScreen(navController) }
        composable(Screen.GoogleRegister.route) { FirebaseGoogleRegisterScreen(navController) }
        composable(Screen.LoginSuccess.route) { LoginSuccessScreen(navController) }
    }
}

@Composable
fun StorageScreensStack() {
    val storageNavController = rememberNavController()

    NavHost(navController = storageNavController, startDestination = Screen.StorageHome.route) {
        composable(Screen.StorageHome.route) { DataStorageScreen(storageNavController) }
        composable(Screen.SharedPreference.route) {}
        composable(Screen.RoomDb.route) { WalletScreensStack() }
    }
}

@Composable
fun WalletScreensStack(viewModel: WalletViewModel = viewModel()) {
    val walletNavController = rememberNavController()
    val walletItems by viewModel.allItems.observeAsState(emptyList())

    NavHost(navController = walletNavController, startDestination = Screen.WalletHome.route) {
        composable(Screen.WalletHome.route) { WalletHomeScreen(walletNavController) }
        composable(Screen.WalletList.route) {
            WalletList(walletItems) { item ->
                walletNavController.navigate("wallet_detail/${item.id}")
            }
        }
        composable(Screen.WalletDetails.route) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull()
            val item = walletItems.find { it.id == itemId }
            if (item != null) WalletDetailsScreen(item)
        }
        composable(Screen.AddWalletItem.route) {
            AddWalletItemScreen { item ->
                viewModel.addItem(item)
                walletNavController.popBackStack() // Go back to the previous screen after adding
            }
        }
    }
}
