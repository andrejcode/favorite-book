package com.andrejmilanovic.favoritebook.ui.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.andrejmilanovic.favoritebook.R
import com.andrejmilanovic.favoritebook.domain.model.Book
import com.andrejmilanovic.favoritebook.data.remote.Result
import com.andrejmilanovic.favoritebook.data.remote.Result.Loading
import com.andrejmilanovic.favoritebook.data.remote.response.BookItem

@Composable
fun DetailsScreen(
    bookId: String,
    navController: NavController,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.book_details)) },
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
            val bookInfo = produceState<Result<BookItem>>(initialValue = Loading) {
                value = viewModel.getBookInfo(bookId)
            }.value

            when (bookInfo) {
                is Result.Success -> BookDetails(
                    bookInfo = bookInfo,
                    navController = navController,
                    viewModel
                )
                is Result.Error -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(id = R.string.unable_to_get_book_info))
                    }
                }
                is Loading -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun BookDetails(
    bookInfo: Result<BookItem>,
    navController: NavController,
    viewModel: DetailsViewModel
) {
    if (bookInfo is Result.Success) {
        val book = bookInfo.data.volumeInfo
        val googleBookId = bookInfo.data.id

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // If there is not book cover image use generic book cover from drawables
            if (book.imageLinks?.thumbnail.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.book_cover),
                    contentDescription = stringResource(id = R.string.book_cover),
                    modifier = Modifier.height(160.dp)
                )
            } else {
                // Use Coin AsyncImage composable to load and display the image
                AsyncImage(
                    model = book.imageLinks?.thumbnail,
                    contentDescription = stringResource(id = R.string.book_cover),
                    modifier = Modifier.height(160.dp)
                )
            }
            Text(
                text = book.title,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.h5
            )
            Text(
                text = if (book.authors!!.size > 1) {
                    stringResource(id = R.string.authors)
                } else {
                    stringResource(id = R.string.author)
                } + " " + book.authors.joinToString(", "),
                style = MaterialTheme.typography.subtitle1
            )
            // Sometimes publishedDate can be null, same goes for categories
            if (!book.publishedDate.isNullOrEmpty()) {
                Text(
                    text = stringResource(R.string.published_date, book.publishedDate),
                    style = MaterialTheme.typography.subtitle1
                )
            }
            if (!book.categories.isNullOrEmpty()) {
                Text(
                    text = stringResource(
                        id = R.string.categories,
                        book.categories.joinToString()
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Text(
                text = stringResource(id = R.string.page_count, book.pageCount),
                style = MaterialTheme.typography.subtitle1
            )
            if (!book.description.isNullOrEmpty()) {
                // Remove HTML tags from description text
                val formatDescription =
                    HtmlCompat.fromHtml(book.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                        .toString()
                val descriptionHeight =
                    LocalContext.current.resources.displayMetrics.heightPixels.dp.times(0.12f)

                Surface(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .height(descriptionHeight),
                    shape = RectangleShape,
                    border = BorderStroke(1.dp, Color.DarkGray)
                ) {
                    LazyColumn(modifier = Modifier.padding(8.dp)) {
                        item {
                            Text(
                                text = formatDescription,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            }
            // Save book to local database
            Button(
                onClick = {
                    viewModel.saveBook(
                        Book(
                            id = googleBookId,
                            title = book.title,
                            authors = if (!book.authors.isNullOrEmpty()) book.authors.toString() else null,
                            image = if (!book.imageLinks?.thumbnail.isNullOrEmpty()) book.imageLinks?.thumbnail else null
                        )
                    )
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}