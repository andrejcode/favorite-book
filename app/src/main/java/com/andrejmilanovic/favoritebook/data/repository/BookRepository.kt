package com.andrejmilanovic.favoritebook.data.repository

import com.andrejmilanovic.favoritebook.data.remote.BookApi
import com.andrejmilanovic.favoritebook.data.remote.Result
import com.andrejmilanovic.favoritebook.data.remote.response.BookItem
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BookApi) {
    suspend fun getBooks(searchQuery: String): Result<List<BookItem>> {
        return try {
            Result.Success(api.getBooks(searchQuery).items)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getBookInfo(bookId: String): Result<BookItem> {
        return try {
            Result.Success(api.getBookInfo(bookId))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}