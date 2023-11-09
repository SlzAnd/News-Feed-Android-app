package com.example.newsfeed.data.local

import androidx.room.TypeConverter
import com.example.newsfeed.domain.model.Source
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromStringToDate(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToString(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun sourceToString(source: Source?): String? {
        return source?.name
    }

    @TypeConverter
    fun stringToSource(value: String?): Source? {
        return value?.let { enumValueOf<Source>(it) }
    }
}