package com.sbeu.news.di

import android.content.Context
import androidx.room.Room
import com.sbeu.news.data.local.NewsDao
import com.sbeu.news.data.local.NewsDatabase
import com.sbeu.news.data.remote.NewsApiService
import com.sbeu.news.data.repository.NewsRepositoryImpl
import com.sbeu.news.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindNewsRepository(
        impl: NewsRepositoryImpl
    ): NewsRepository

    companion object {

        @Singleton
        @Provides
        fun provideJson(): Json {
            return Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        }

        @Singleton
        @Provides
        fun provideConverterFactory(
            json: Json
        ): Converter.Factory {
            return json.asConverterFactory("application/json".toMediaType())
        }

        @Singleton
        @Provides
        fun provideRetrofit(
            converterFactory: Converter.Factory
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://newsapi.org")
                .addConverterFactory(converterFactory)
                .build()
        }

        @Singleton
        @Provides
        fun provideApiService(
            retrofit: Retrofit
        ): NewsApiService {
            return retrofit.create()
        }

        @Singleton
        @Provides
        fun provideDatabase(
            @ApplicationContext context: Context
        ): NewsDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = NewsDatabase::class.java,
                name = "news.db"
            ).fallbackToDestructiveMigration(dropAllTables = true).build()
        }

        @Singleton
        @Provides
        fun provideNewsDao(
            database: NewsDatabase
        ): NewsDao {
            return database.newsDao()
        }
    }
}