package com.andrejmilanovic.favoritebook.data.local

import androidx.room.*
import com.andrejmilanovic.favoritebook.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM book_table ORDER BY id ASC")
    fun getBooks(): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)
}