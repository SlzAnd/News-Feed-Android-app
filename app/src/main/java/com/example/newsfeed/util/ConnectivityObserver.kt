package com.example.newsfeed.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(coroutineScope: CoroutineScope): Flow<Boolean>
}