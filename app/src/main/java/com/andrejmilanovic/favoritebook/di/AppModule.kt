package com.andrejmilanovic.favoritebook.di

import android.content.Context
import androidx.room.Room
import com.andrejmilanovic.favoritebook.data.local.BookDao
import com.andrejmilanovic.favoritebook.data.local.BookDatabase
import com.andrejmilanovic.favoritebook.data.remote.BookApi
import com.andrejmilanovic.favoritebook.data.repository.BookRepository
import com.andrejmilanovic.favoritebook.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideBookApi(): BookApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, BookDatabase::class.java, "book_database").build()

    @Singleton
    @Provides
    fun provideDao(database: BookDatabase) = database.bookDao()

    @Singleton
    @Provides
    fun provideBookRepository(api: BookApi, dao: BookDao) = BookRepository(api, dao)
}