package com.andrejmilanovic.favoritebook

sealed class BookScreen(val route: String) {
    object Auth: BookScreen("auth")
    object Home: BookScreen("home")
    object Details: BookScreen("details/{bookId}")
    object Search: BookScreen("search")
}