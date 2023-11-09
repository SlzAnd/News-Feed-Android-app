package com.example.newsfeed.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.newsfeed.R
import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.Source
import com.example.newsfeed.presentation.navigation.BottomNavItem
import com.example.newsfeed.ui.theme.concrete
import com.example.newsfeed.ui.theme.dustyGray
import com.example.newsfeed.ui.theme.lightGray
import com.example.newsfeed.ui.theme.poppinsFontFamily
import com.example.newsfeed.ui.theme.webOrange
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val items = listOf(
        BottomNavItem.AllNewsFeeds,
        BottomNavItem.Groups,
        BottomNavItem.Bookmarked
    )

    NavigationBar(
        containerColor = dustyGray
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route)
                },
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = webOrange,
                    unselectedIconColor = concrete,
                    indicatorColor = dustyGray
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    modifier: Modifier,
    hasBookmarkIcon: Boolean,
    isBookmarked: Boolean,
    onBookmarkEvent: () -> Unit
) {
    val startPadding = if (hasBookmarkIcon) 40.dp else 0.dp

    TopAppBar(
        modifier = modifier
            .clip(shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)),
        title = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = startPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight(500)
                )
            }
        },
        actions = {
            if (hasBookmarkIcon) {
                IconButton(onClick = {
                    onBookmarkEvent()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.bookmark),
                        contentDescription = "bookmark",
                        tint = if (isBookmarked) Color.Black else Color.Unspecified
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = webOrange
        )
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsItem(
    modifier: Modifier = Modifier,
    news: News,
    isBookmarked: Boolean,
    onBookmarkEvent: () -> Unit
) {
    val isSaved = remember {
        mutableStateOf(isBookmarked)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(132.dp)
            .background(lightGray),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = modifier
                .padding(start = 26.dp)
                .padding(vertical = 9.dp)
                .width(114.dp)
                .height(114.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = lightGray
            )
        ) {
            Glide.with(LocalView.current)
                .load(news.imageUrl)
                .preload()

            GlideImage(
                model = news.imageUrl,
                contentDescription = news.title,
                contentScale = ContentScale.Crop
            ) {
                it
                    .load(news.imageUrl)
                    .error(R.drawable.no_pictures)
            }
        }

        Column(
            modifier = modifier
                .padding(start = 14.dp, top = 12.dp, end = 23.dp)
                .fillMaxSize()
        ) {
            SourceBadge(
                modifier = modifier
                    .align(Alignment.End),
                source = news.newsSource,
                isEnabledSource = true,
                onClickEvent = {}
            )

            Text(
                text = dateTimeConverter(news.pubDate),
                fontFamily = poppinsFontFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.W400,
                color = dustyGray
            )

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Text(
                    modifier = modifier
                        .weight(1f)
                        .fillMaxSize(),
                    text = news.title,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W600,
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    color = Color.Black,
                    maxLines = 3,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )

                IconButton(
                    onClick = {
                        isSaved.value = !isSaved.value
                        onBookmarkEvent()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.bookmark),
                        contentDescription = "save in bookmarks",
                        tint = if (isSaved.value) webOrange else Color.Unspecified
                    )
                }
            }
        }
    }
}

private fun dateTimeConverter(pubDate: LocalDateTime): String {
    val currentDateTime = LocalDateTime.now()
    val pubZonedDateTime = pubDate.atZone(ZoneId.systemDefault())
    val currentZonedDateTime = currentDateTime.atZone(ZoneId.systemDefault())

    val duration = Duration.between(pubZonedDateTime, currentZonedDateTime)

    return when (val daysAgo = duration.toDays()) {
        0L -> {
            val hours = duration.toHours()
            if (hours > 0) {
                "$hours hours ago"
            } else {
                val minutes = duration.toMinutes()
                "$minutes minutes ago"
            }
        }

        1L -> "Yesterday"
        in 2L..6L -> "$daysAgo days ago"
        else -> {
            val weeksAgo = daysAgo / 7
            if (weeksAgo < 4) {
                "$weeksAgo weeks ago"
            } else {
                val monthsAgo = weeksAgo / 4
                "$monthsAgo months ago"
            }
        }
    }
}

@Composable
fun SourceBadge(
    modifier: Modifier = Modifier,
    source: Source,
    isEnabledSource: Boolean,
    onClickEvent: (isEnabled: Boolean) -> Unit
) {
    val isEnabled = remember {
        mutableStateOf(isEnabledSource)
    }
    val color = if (isEnabled.value) source.color else Color.LightGray

    Box(
        modifier = modifier
            .clickable {
                isEnabled.value = !isEnabled.value
                onClickEvent(isEnabled.value)
            }
            .height(32.dp)
            .background(color, RoundedCornerShape(10.dp))
            .border(1.dp, source.color, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = modifier.padding(horizontal = 15.dp),
            text = source.simpleName,
            color = if (isEnabled.value) Color.White else Color.Gray,
            fontFamily = poppinsFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500
        )
    }
}

@Composable
fun NetworkConnectionMessage(
    modifier: Modifier = Modifier,
    message: String,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(lightGray)
            .shadow(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.W400,
                color = dustyGray
            )
        )
    }
}