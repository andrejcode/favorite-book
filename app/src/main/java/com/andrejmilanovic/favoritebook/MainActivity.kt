package com.andrejmilanovic.favoritebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andrejmilanovic.favoritebook.BookScreen.*
import com.andrejmilanovic.favoritebook.ui.auth.AuthScreen
import com.andrejmilanovic.favoritebook.ui.home.HomeScreen
import com.andrejmilanovic.favoritebook.ui.search.SearchScreen
import com.andrejmilanovic.favoritebook.ui.theme.FavoriteBookTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookApp()
        }
    }
}

@Composable
fun BookApp() {
    FavoriteBookTheme {
        val navController = rememberNavController()
        BookNavHost(navController = navController)
    }
}

@Composable
fun BookNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Auth.name) {
        composable(Auth.name) {
            AuthScreen {
                navController.navigate(Home.name) {
                    popUpTo(Auth.name) {
                        inclusive = true
                    }
                }
            }
        }
        composable(Home.name) {
            HomeScreen(navController)
        }
        composable(Search.name) {
            SearchScreen()
        }
    }
}