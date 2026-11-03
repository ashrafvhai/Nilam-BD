package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BanglaUtils
import com.example.model.Language
import com.example.model.NilamStrings
import com.example.ui.theme.FbBlue
import com.example.ui.theme.FbTextSecondary
import com.example.ui.theme.NilamErrorRed
import com.example.viewmodel.AppScreen

@Composable
fun NilamBottomNavBar(
    currentScreen: AppScreen,
    lang: Language,
    unreadInboxCount: Int,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)

        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            // 1. Home (News Feed icon)
            NavigationBarItem(
                selected = currentScreen == AppScreen.HOME,
                onClick = { onNavigate(AppScreen.HOME) },
                icon = {
                    Icon(
                        imageVector = if (currentScreen == AppScreen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = NilamStrings.home(lang)
                    )
                },
                label = {
                    Text(
                        text = NilamStrings.home(lang),
                        fontSize = 11.sp,
                        fontWeight = if (currentScreen == AppScreen.HOME) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = FbBlue,
                    selectedTextColor = FbBlue,
                    unselectedIconColor = FbTextSecondary,
                    unselectedTextColor = FbTextSecondary,
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier.testTag("nav_home")
            )

            // 2. Search
            NavigationBarItem(
                selected = currentScreen == AppScreen.SEARCH,
                onClick = { onNavigate(AppScreen.SEARCH) },
                icon = {
                    Icon(
                        imageVector = if (currentScreen == AppScreen.SEARCH) Icons.Filled.Search else Icons.Outlined.Search,
                        contentDescription = NilamStrings.search(lang)
                    )
                },
                label = {
                    Text(
                        text = NilamStrings.search(lang),
                        fontSize = 11.sp,
                        fontWeight = if (currentScreen == AppScreen.SEARCH) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = FbBlue,
                    selectedTextColor = FbBlue,
                    unselectedIconColor = FbTextSecondary,
                    unselectedTextColor = FbTextSecondary,
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier.testTag("nav_search")
            )

            // 3. Post (+) Center Action
            NavigationBarItem(
                selected = currentScreen == AppScreen.POST_CREATE,
                onClick = { onNavigate(AppScreen.POST_CREATE) },
                icon = {
                    Surface(
                        shape = CircleShape,
                        color = FbBlue,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = NilamStrings.post(lang),
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = NilamStrings.post(lang),
                        fontSize = 11.sp,
                        fontWeight = if (currentScreen == AppScreen.POST_CREATE) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = FbBlue,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = FbTextSecondary,
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier.testTag("nav_post")
            )

            // 4. Inbox (Messenger chat style)
            NavigationBarItem(
                selected = currentScreen == AppScreen.INBOX || currentScreen == AppScreen.CHAT_DETAIL,
                onClick = { onNavigate(AppScreen.INBOX) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (unreadInboxCount > 0) {
                                Badge(
                                    containerColor = NilamErrorRed,
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = if (lang == Language.BN)
                                            BanglaUtils.toBanglaDigits(unreadInboxCount.toString())
                                        else
                                            unreadInboxCount.toString()
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (currentScreen == AppScreen.INBOX || currentScreen == AppScreen.CHAT_DETAIL)
                                Icons.Filled.ChatBubble
                            else
                                Icons.Outlined.ChatBubbleOutline,
                            contentDescription = NilamStrings.inbox(lang)
                        )
                    }
                },
                label = {
                    Text(
                        text = NilamStrings.inbox(lang),
                        fontSize = 11.sp,
                        fontWeight = if (currentScreen == AppScreen.INBOX) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = FbBlue,
                    selectedTextColor = FbBlue,
                    unselectedIconColor = FbTextSecondary,
                    unselectedTextColor = FbTextSecondary,
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier.testTag("nav_inbox")
            )

            // 5. Profile
            NavigationBarItem(
                selected = currentScreen == AppScreen.PROFILE,
                onClick = { onNavigate(AppScreen.PROFILE) },
                icon = {
                    Icon(
                        imageVector = if (currentScreen == AppScreen.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                        contentDescription = NilamStrings.profile(lang)
                    )
                },
                label = {
                    Text(
                        text = NilamStrings.profile(lang),
                        fontSize = 11.sp,
                        fontWeight = if (currentScreen == AppScreen.PROFILE) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = FbBlue,
                    selectedTextColor = FbBlue,
                    unselectedIconColor = FbTextSecondary,
                    unselectedTextColor = FbTextSecondary,
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier.testTag("nav_profile")
            )
        }
    }
}
