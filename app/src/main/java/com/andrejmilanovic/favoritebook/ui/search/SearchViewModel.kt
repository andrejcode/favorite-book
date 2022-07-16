package com.andrejmilanovic.favoritebook.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrejmilanovic.favoritebook.data.remote.Result
import com.andrejmilanovic.favoritebook.data.remote.Result.*
import com.andrejmilanovic.favoritebook.data.remote.response.BookItem
import com.andrejmilanovic.favoritebook.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {
    var books: List<BookItem> by mutableStateOf(listOf())
    var isLoading by mutableStateOf(false)

    init {
        // Search for android books when ViewModel is created
        searchBooks("android")
    }

    fun searchBooks(searchQuery: String) {
        viewModelScope.launch {
            isLoading = true
            when (val result = repository.getBooks(searchQuery)) {
                is Success -> {
                    books = result.data
                    isLoading = false
                }
                is Error -> {
                    // TODO display error message
                    isLoading = false
                }
                is Loading -> {
                    isLoading = true
                }
            }
        }
    }
}