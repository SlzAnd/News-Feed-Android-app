package com.example.newsfeed.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.newsfeed.data.NewsRepositoryImpl
import com.example.newsfeed.data.datastore.StoreSettings
import com.example.newsfeed.data.local.NewsDao
import com.example.newsfeed.data.local.NewsDatabase
import com.example.newsfeed.data.remote.NewsFeedClient
import com.example.newsfeed.data.remote.ResultCallAdapterFactory
import com.example.newsfeed.domain.repository.NewsRepository
import com.example.newsfeed.domain.use_case.GetAllNews
import com.example.newsfeed.domain.use_case.GetNewsById
import com.example.newsfeed.domain.use_case.GetNewsFromSource
import com.example.newsfeed.domain.use_case.NewsUseCases
import com.example.newsfeed.domain.use_case.RefreshNewsFeed
import com.example.newsfeed.domain.use_case.UpdateNewsInCache
import com.example.newsfeed.util.ConnectivityObserver
import com.example.newsfeed.util.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): NewsFeedClient {
        return NewsFeedClient(retrofit)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        apiClient: NewsFeedClient,
        dao: NewsDao,
        @ApplicationContext context: Context,
        dataStore: StoreSettings
    ): NewsRepository {
        return NewsRepositoryImpl(apiClient, dao, context, dataStore)
    }

    @Provides
    @Singleton
    fun provideNewsUseCases(repository: NewsRepository): NewsUseCases {
        return NewsUseCases(
            GetNewsFromSource(repository),
            UpdateNewsInCache(repository),
            RefreshNewsFeed(repository),
            GetNewsById(repository),
            GetAllNews(repository)
        )
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(application: Application): NewsDatabase {
        return Room.databaseBuilder(
            application,
            NewsDatabase::class.java,
            NewsDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(database: NewsDatabase): NewsDao {
        return database.dao()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): StoreSettings {
        return StoreSettings(context)
    }

    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dev.ua/rss/")
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(Persister()))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()
    }
}