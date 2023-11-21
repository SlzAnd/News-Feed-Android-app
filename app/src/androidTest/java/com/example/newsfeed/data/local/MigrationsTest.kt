package com.example.newsfeed.data.local

import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationsTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        NewsDatabase::class.java
    )
    private val TEST_DB = "migration-test"

    companion object {
        val Test_migration_1_2: Migration =
            Migration(startVersion = 1, endVersion = 2) { database ->
                database.execSQL("ALTER TABLE news ADD COLUMN testMigration TEXT NOT NULL DEFAULT 'test'")
            }

        val Test_migration_2_3: Migration =
            Migration(startVersion = 2, endVersion = 3) { database ->
                database.execSQL(
                    "CREATE TABLE temp (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "url TEXT NOT NULL," +
                            "newsSource TEXT NOT NULL," +
                            "title TEXT NOT NULL," +
                            "imageUrl TEXT NOT NULL," +
                            "pubDate TEXT NOT NULL," +
                            "isBookmarked INTEGER NOT NULL)"
                )

                database.execSQL(
                    "INSERT INTO temp (id, url, newsSource, title, imageUrl, pubDate, isBookmarked) " +
                            "SELECT id, url, newsSource, title, imageUrl, pubDate, isBookmarked FROM News"
                )

                database.execSQL("DROP TABLE IF EXISTS News")

                database.execSQL("ALTER TABLE temp RENAME TO News")
            }
    }


    @Test
    fun migrate1To2_addNewColumn() {
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL("INSERT INTO news VALUES(0, 'url1', 'dev_ua', 'title1', 'imageUrl1', 'pubDate1', false)")
        }

        val migratedDatabase = helper.runMigrationsAndValidate(TEST_DB, 2, true, Test_migration_1_2)

        val cursor = migratedDatabase.query("SELECT * FROM news")

        assertThat(cursor.columnNames.size).isEqualTo(8)
        assertThat(cursor.columnNames[7]).isEqualTo("testMigration")
    }

    @Test
    fun migrate2To3_deleteColumn() {
        helper.createDatabase(TEST_DB, 2).apply {
            execSQL("INSERT INTO news VALUES(0, 'url1', 'dev_ua', 'title1', 'imageUrl1', 'pubDate1', false, 'test123')")
        }

        val migratedDatabase = helper.runMigrationsAndValidate(
            TEST_DB, 3, true, Test_migration_2_3
        )

        val cursor = migratedDatabase.query("SELECT * FROM news")

        assertThat(cursor.columnNames.size).isEqualTo(7)
        assertThat(cursor.columnNames).isEqualTo(
            arrayOf(
                "id",
                "url",
                "newsSource",
                "title",
                "imageUrl",
                "pubDate",
                "isBookmarked"
            )
        )
    }
}