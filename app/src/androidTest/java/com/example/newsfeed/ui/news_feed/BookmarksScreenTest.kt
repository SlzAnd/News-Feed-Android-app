package com.example.newsfeed.ui.news_feed

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.newsfeed.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class BookmarksScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.waitForIdle()
    }

    @Test
    fun test_navigateToBookmarksScreen() {
        composeTestRule.onNodeWithContentDescription("bookmarked").performClick()
        composeTestRule.onNodeWithText("Saved").assertExists()
    }

    @Test
    fun test_addNewsToBookmarks() {
        // check if news wasn't saved to bookmarks
        composeTestRule.onNodeWithContentDescription("bookmarked").performClick()
        composeTestRule.onAllNodesWithTag("newsItem").assertAll(isNotEnabled())

        // add one news to bookmarks
        composeTestRule.onNodeWithContentDescription("all_news").performClick()
        composeTestRule.onAllNodesWithContentDescription("save in bookmarks")[0].performClick()

        // check that news was saved in bookmarks
        composeTestRule.onNodeWithContentDescription("bookmarked").performClick()
        composeTestRule.onAllNodesWithTag("newsItem").assertCountEquals(1)
    }

    @Test
    fun test_removeNewsFromBookmarks() {
        // save in bookmarks
        composeTestRule.onAllNodesWithContentDescription("save in bookmarks")[0].performClick()

        // check that was saved
        composeTestRule.onNodeWithContentDescription("bookmarked").performClick()
        composeTestRule.onNodeWithTag("newsItem").assertExists()

        // remove from bookmarks and check that this news was disappeared
        composeTestRule.onAllNodesWithContentDescription("save in bookmarks")[0].performClick()
        composeTestRule.onNodeWithTag("newsItem").assertIsNotDisplayed()
    }
}