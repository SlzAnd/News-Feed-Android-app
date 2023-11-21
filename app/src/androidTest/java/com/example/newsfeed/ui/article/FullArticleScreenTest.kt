package com.example.newsfeed.ui.article

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.newsfeed.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class FullArticleScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_initialLoadingIconIsDisplayed() {
        composeTestRule.onAllNodesWithTag("newsItem")[0].performClick()
        composeTestRule.onNodeWithContentDescription("loading icon").assertExists()
        composeTestRule.onNodeWithContentDescription("bookmark").assertExists()
    }

    @Test
    fun test_initialScreenState() {
        composeTestRule.onAllNodesWithTag("newsItem")[0].performClick()
        composeTestRule.onNodeWithContentDescription("bookmark").assertExists()
    }

    @Test
    fun test_webViewDisplayed() {
        composeTestRule.onAllNodesWithTag("newsItem")[0].performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("webView").assertExists()
    }

}