package com.andrejmilanovic.favoritebook.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrejmilanovic.favoritebook.data.model.Book
import com.andrejmilanovic.favoritebook.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {
    private val _savedBooks = MutableStateFlow<List<Book>>(listOf())
    val savedBooks: StateFlow<List<Book>> = _savedBooks

    var loading by mutableStateOf(false)

    init {
        getSavedBooks()
    }

    private fun getSavedBooks() {
        try {
            loading = true
            viewModelScope.launch {
                repository.getSavedBooks.collect {
                    _savedBooks.value = it
                }
            }
        } catch (e: Exception) {
            // TODO show error message
        } finally {
            loading = false
        }
    }
}