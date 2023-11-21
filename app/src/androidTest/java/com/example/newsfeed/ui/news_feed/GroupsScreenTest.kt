package com.example.newsfeed.ui.news_feed

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
class GroupsScreenTest {
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
    fun test_navigateToGroupsScreen() {
        composeTestRule.onNodeWithContentDescription("groups").performClick()
        composeTestRule.onNodeWithText("All groups").assertExists()
    }

    @Test
    fun test_selectingOneNewsSourceUploadNews() {
        composeTestRule.onNodeWithContentDescription("groups").performClick()
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag("dev_ua_badge")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("groupNewsItem").assertDoesNotExist()
        composeTestRule.onNodeWithTag("dev_ua_badge").performClick()
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag("groupNewsItem")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun test_unselectingAllNewsSourcesShowEmptyScreen() {
        composeTestRule.onNodeWithContentDescription("groups").performClick()
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag("dev_ua_badge")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("dev_ua_badge").performClick()
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag("groupNewsItem")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onAllNodesWithTag("groupNewsItem").assertAll(isEnabled())
        composeTestRule.onNodeWithTag("dev_ua_badge").performClick()

        composeTestRule.onAllNodesWithTag("groupNewsItem")[0].assertIsNotDisplayed()
    }
}