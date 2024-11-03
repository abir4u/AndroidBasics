package com.abir.androidbasicpart1.composables.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abir.androidbasicpart1.screens.FirebaseLoginHomeScreen
import com.abir.androidbasicpart1.screens.FirebaseEmailLoginScreen
import com.abir.androidbasicpart1.screens.FirebaseEmailRegisterScreen
import com.abir.androidbasicpart1.screens.FirebaseRegisterHomeScreen

sealed class Screen(val route: String) {
    object LoginHome : Screen("loginHome")
    object RegisterHome : Screen("registerHome")
    object EmailLogin : Screen("emailLogin")
    object EmailRegister : Screen("emailRegister")
}

@Composable
fun FirebaseNavigations() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginHome.route) {
        composable(Screen.LoginHome.route) { FirebaseLoginHomeScreen(navController) }
        composable(Screen.RegisterHome.route) { FirebaseRegisterHomeScreen(navController) }
        composable(Screen.EmailLogin.route) { FirebaseEmailLoginScreen(navController) }
        composable(Screen.EmailRegister.route) { FirebaseEmailRegisterScreen(navController) }
    }
}