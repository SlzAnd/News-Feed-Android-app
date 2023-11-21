package com.example.newsfeed.ui.news_feed

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.newsfeed.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NewsFeedScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_initialAllFeedsScreenState() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("refreshIndicator").assertExists()
        composeTestRule.onNodeWithText("All Feeds").assertExists()
        composeTestRule.onNodeWithText("News Feed").assertExists()
        composeTestRule.onNodeWithContentDescription("all_news").assertIsEnabled()
        composeTestRule.onAllNodesWithTag("newsItem").assertAll(isEnabled())
    }
}