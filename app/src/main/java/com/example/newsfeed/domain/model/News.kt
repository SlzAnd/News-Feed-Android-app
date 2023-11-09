package com.example.newsfeed.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class News(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val url: String,
    val newsSource: Source,
    val title: String,
    val imageUrl: String,
    val pubDate: LocalDateTime,
    val isBookmarked: Boolean
)
