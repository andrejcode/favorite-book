package com.andrejmilanovic.favoritebook.di

import com.andrejmilanovic.favoritebook.data.remote.BookApi
import com.andrejmilanovic.favoritebook.data.repository.BookRepository
import com.andrejmilanovic.favoritebook.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideBookRepository(api: BookApi) = BookRepository(api)
}