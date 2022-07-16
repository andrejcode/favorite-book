package com.andrejmilanovic.favoritebook.data.remote

/**
 * A sealed class that encapsulates successful outcome with a value of type [T]
 * or a failure with exception
 */
sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}