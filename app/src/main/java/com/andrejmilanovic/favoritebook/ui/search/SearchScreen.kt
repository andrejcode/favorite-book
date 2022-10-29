package com.andrejmilanovic.favoritebook.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.andrejmilanovic.favoritebook.BookScreen.Details
import com.andrejmilanovic.favoritebook.R
import com.andrejmilanovic.favoritebook.data.remote.response.BookItem
import com.andrejmilanovic.favoritebook.ui.component.InputField

@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.search_books)) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.arrow_back)
                    )
                }
            })
    }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val searchQuery = rememberSaveable { mutableStateOf("") }
            val focusManager = LocalFocusManager.current

            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Search input field
                InputField(
                    valueState = searchQuery,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(id = R.string.search)
                        )
                    },
                    label = stringResource(id = R.string.search),
                    modifier = Modifier,
                    enabled = true,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        viewModel.searchBooks(searchQuery.value.trim())
                    }),
                    visualTransformation = VisualTransformation.None,
                    trailingIcon = null
                )
                SearchBookList(viewModel = viewModel, navController = navController)
            }
        }
    }
}

@Composable
fun SearchBookList(viewModel: SearchViewModel, navController: NavController) {
    if (viewModel.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            items(items = viewModel.books) { book ->
                SearchBookRow(book, navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchBookRow(book: BookItem, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RectangleShape,
        elevation = 4.dp,
        onClick = {
            navController.navigate(Details.route + "/${book.id}")
        }) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.Top) {
            // If there is not book cover image use generic book cover from drawables
            if (book.volumeInfo.imageLinks?.thumbnail.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.book_cover),
                    contentDescription = stringResource(id = R.string.book_cover),
                    modifier = Modifier.height(160.dp)
                )
            } else {
                // Use Coin AsyncImage composable to load and display the image
                AsyncImage(
                    model = book.volumeInfo.imageLinks?.thumbnail,
                    contentDescription = stringResource(id = R.string.book_cover),
                    modifier = Modifier
                        .height(160.dp)
                        .widthIn(max = 120.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
                Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
                if (!book.volumeInfo.authors.isNullOrEmpty()) {
                    Text(
                        text = book.volumeInfo.authors.joinToString(", "),
                        modifier = Modifier.padding(top = 4.dp),
                        overflow = TextOverflow.Clip,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}