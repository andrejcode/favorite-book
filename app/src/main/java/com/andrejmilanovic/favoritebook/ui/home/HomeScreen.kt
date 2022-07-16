package com.andrejmilanovic.favoritebook.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.andrejmilanovic.favoritebook.BookScreen
import com.andrejmilanovic.favoritebook.R
import com.andrejmilanovic.favoritebook.data.model.Book
import com.andrejmilanovic.favoritebook.ui.component.RoundedButton
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
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

                val savedBooks by viewModel.savedBooks.collectAsState()
                if (savedBooks.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "No books added yet", style = MaterialTheme.typography.body1)
                    }
                } else {
                    if (viewModel.loading) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyColumn {
                            items(items = savedBooks) { book ->
                                BookCard(book = book, onClick = {})
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookCard(book: Book, onClick: (Book) -> Unit) {
    Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 4.dp
    ) {
        Column {
            Row(horizontalArrangement = Arrangement.Center) {
                if (book.image.isNullOrEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.book_cover),
                        contentDescription = stringResource(id = R.string.book_cover),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 14.dp)
                            .height(200.dp)
                    )
                } else {
                    // Use Coin AsyncImage composable to load and display the image
                    AsyncImage(
                        model = book.image,
                        contentDescription = stringResource(id = R.string.book_cover),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 14.dp)
                            .height(200.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { onClick.invoke(book) }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = stringResource(id = R.string.favorite),
                        modifier = Modifier
                            .padding(16.dp)
                            .size(50.dp)
                    )
                }
            }
            Text(
                text = book.title,
                modifier = Modifier.padding(start = 16.dp, top = 10.dp, end = 8.dp),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (!book.authors.isNullOrEmpty()) {
                Text(
                    text = book.authors,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 4.dp,
                        end = 8.dp,
                        bottom = 16.dp
                    ),
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row {
                Spacer(modifier = Modifier.weight(1f))
                RoundedButton(label = stringResource(id = R.string.details)) {

                }
            }
        }
    }
}