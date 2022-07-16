package com.andrejmilanovic.favoritebook.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_table")
data class Book(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val authors: String?,
    val image: String?
)
