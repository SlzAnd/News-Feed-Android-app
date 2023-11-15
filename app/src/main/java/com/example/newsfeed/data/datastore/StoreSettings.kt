package com.example.newsfeed.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import javax.inject.Inject

class StoreSettings @Inject constructor(
    private val context: Context
) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "newsRepositoryStorage"
        )
        val LAST_UPDATE_TIME = stringPreferencesKey("lastUpdateTime")
    }

    fun getLastUpdateTime(): String {
        return runBlocking {
            context.dataStore.data.first()[LAST_UPDATE_TIME] ?: LocalDateTime.MIN.toString()
        }
    }

    suspend fun setLastUpdateTime(time: String) {
        context.dataStore.edit { pref ->
            pref[LAST_UPDATE_TIME] = time
        }
    }
}