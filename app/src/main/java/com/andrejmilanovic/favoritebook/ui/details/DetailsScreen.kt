package com.andrejmilanovic.favoritebook.ui.details

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun DetailsScreen(bookId: String, navController: NavController) {
    Text(text = bookId)
}