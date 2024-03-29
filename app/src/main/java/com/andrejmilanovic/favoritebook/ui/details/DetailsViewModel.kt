package com.andrejmilanovic.favoritebook.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrejmilanovic.favoritebook.domain.model.Book
import com.andrejmilanovic.favoritebook.data.remote.Result
import com.andrejmilanovic.favoritebook.data.remote.response.BookItem
import com.andrejmilanovic.favoritebook.data.repository.BookRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: BookRepositoryImpl) : ViewModel() {
    suspend fun getBookInfo(bookId: String): Result<BookItem> {
        return repository.getBookInfo(bookId)
    }

    fun saveBook(book: Book) {
        viewModelScope.launch {
            repository.saveBook(book)
        }
    }
}