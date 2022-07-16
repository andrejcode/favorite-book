package com.andrejmilanovic.favoritebook.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andrejmilanovic.favoritebook.data.model.Book

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}