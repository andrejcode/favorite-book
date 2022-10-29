package com.andrejmilanovic.favoritebook.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrejmilanovic.favoritebook.domain.model.Book
import com.andrejmilanovic.favoritebook.data.repository.BookRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: BookRepositoryImpl) : ViewModel() {
    private val _savedBooks = MutableStateFlow<List<Book>>(listOf())
    val savedBooks: StateFlow<List<Book>> = _savedBooks

    var isLoading by mutableStateOf(false)

    init {
        getSavedBooks()
    }

    private fun getSavedBooks() {
        try {
            isLoading = true
            viewModelScope.launch {
                repository.getSavedBooks.collect {
                    _savedBooks.value = it
                }
            }
        } catch (e: Exception) {
            // TODO show error message
        } finally {
            isLoading = false
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
        }
    }
}