package com.example.newsfeed.data.local

import androidx.room.migration.Migration

class Migrations {

    companion object {
        val Test_migration_1_2: Migration = Migration(startVersion = 1, endVersion = 2) { database ->
            database.execSQL("ALTER TABLE news ADD COLUMN testMigration TEXT")
        }
    }
}