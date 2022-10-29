package com.andrejmilanovic.favoritebook.domain.repository

import com.andrejmilanovic.favoritebook.data.remote.Result
import com.andrejmilanovic.favoritebook.data.remote.response.BookItem
import com.andrejmilanovic.favoritebook.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    val getSavedBooks: Flow<List<Book>>

    suspend fun saveBook(book: Book)

    suspend fun deleteBook(book: Book)

    suspend fun getBooks(searchQuery: String): Result<List<BookItem>>

    suspend fun getBookInfo(bookId: String): Result<BookItem>
}