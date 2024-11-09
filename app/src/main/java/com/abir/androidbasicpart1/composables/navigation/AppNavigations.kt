package com.abir.androidbasicpart1.composables.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abir.androidbasicpart1.screens.firebase.LoginSuccessScreen
import com.abir.androidbasicpart1.screens.firebase.authentication.FirebaseHomeScreen
import com.abir.androidbasicpart1.screens.firebase.authentication.FirebaseLoginHomeScreen
import com.abir.androidbasicpart1.screens.firebase.authentication.email.FirebaseEmailLoginScreen
import com.abir.androidbasicpart1.screens.firebase.authentication.email.FirebaseEmailRegisterScreen
import com.abir.androidbasicpart1.screens.firebase.authentication.FirebaseRegisterHomeScreen
import com.abir.androidbasicpart1.screens.firebase.authentication.phone.FirebasePhoneLoginScreen
import com.abir.androidbasicpart1.screens.firebase.authentication.phone.FirebasePhoneRegisterScreen

sealed class Screen(val route: String) {
    data object FirebaseHome : Screen("firebaseHome")
    data object LoginHome : Screen("loginHome")
    data object RegisterHome : Screen("registerHome")
    data object EmailLogin : Screen("emailLogin")
    data object EmailRegister : Screen("emailRegister")
    data object PhoneLogin : Screen("phoneLogin")
    data object PhoneRegister : Screen("phoneRegister")
    data object LoginSuccess : Screen("loginSuccess")
}

@Composable
fun FirebaseNavigations() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.FirebaseHome.route) {
        composable(Screen.FirebaseHome.route) { FirebaseHomeScreen(navController) }
        composable(Screen.LoginHome.route) { FirebaseLoginHomeScreen(navController) }
        composable(Screen.RegisterHome.route) { FirebaseRegisterHomeScreen(navController) }
        composable(Screen.EmailLogin.route) { FirebaseEmailLoginScreen(navController) }
        composable(Screen.EmailRegister.route) { FirebaseEmailRegisterScreen(navController) }
        composable(Screen.PhoneLogin.route) { FirebasePhoneLoginScreen(navController) }
        composable(Screen.PhoneRegister.route) { FirebasePhoneRegisterScreen(navController) }
        composable(Screen.LoginSuccess.route) { LoginSuccessScreen(navController) }
    }
}