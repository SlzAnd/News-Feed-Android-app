package com.example.newsfeed.presentation.news_feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.newsfeed.R
import com.example.newsfeed.domain.model.Source
import com.example.newsfeed.presentation.components.BottomNavigationBar
import com.example.newsfeed.presentation.components.CustomTopAppBar
import com.example.newsfeed.presentation.components.SourceBadge
import com.example.newsfeed.ui.theme.poppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                modifier = Modifier,
                hasBookmarkIcon = false,
                isBookmarked = false,
                onBookmarkEvent = {}
            )
        },
        bottomBar = { BottomNavigationBar(navController) }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp, start = 10.dp, bottom = 22.dp),
                text = stringResource(id = R.string.all_groups),
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SourceBadge(
                    source = Source.FOX_NEWS,
                    isEnabledSource = true,
                    onClickEvent = {}
                )
                SourceBadge(
                    source = Source.NYT,
                    isEnabledSource = true,
                    onClickEvent = {}
                )
                SourceBadge(
                    source = Source.DEV_UA,
                    isEnabledSource = true,
                    onClickEvent = {}
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SourceBadge(
                    source = Source.THE_GUARDIAN,
                    isEnabledSource = true,
                    onClickEvent = {}
                )
                SourceBadge(
                    source = Source.SKY_SPORTS,
                    isEnabledSource = true,
                    onClickEvent = {}
                )
            }
        }

    }
}