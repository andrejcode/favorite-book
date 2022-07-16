package com.andrejmilanovic.favoritebook.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.andrejmilanovic.favoritebook.BookScreen
import com.andrejmilanovic.favoritebook.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.favorite_books))
        }, actions = {
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut().run {
                    navController.navigate(BookScreen.Auth.name) {
                        popUpTo(BookScreen.Home.name) {
                            inclusive = true
                        }
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = stringResource(id = R.string.logout)
                )
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = { navController.navigate(BookScreen.Search.name) },
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.add_book)
            )
        }
    }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Top) {
                val email = FirebaseAuth.getInstance().currentUser?.email
                Text(
                    text = stringResource(
                        id = R.string.welcome_user,
                        email?.split("@")?.get(0) ?: "N/A"
                    ),
                    fontSize = 24.sp
                )

                // TODO add list of favorite books
            }
        }
    }
}