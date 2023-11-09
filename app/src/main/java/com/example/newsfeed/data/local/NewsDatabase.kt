package com.example.newsfeed.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsfeed.domain.model.News

@Database(entities = [News::class], version = 1)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun dao(): NewsDao

    companion object {
        const val DATABASE_NAME = "news.db"
    }
}