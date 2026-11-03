package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BanglaUtils
import com.example.model.Language
import com.example.model.NilamStrings
import com.example.ui.theme.*

@Composable
fun TrustedBadge(
    modifier: Modifier = Modifier,
    lang: Language = Language.BN,
    isSmall: Boolean = false
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp)),
        color = FbBlueLight,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = if (isSmall) 6.dp else 8.dp,
                vertical = if (isSmall) 2.dp else 3.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Verified",
                tint = FbBlue,
                modifier = Modifier.size(if (isSmall) 12.dp else 14.dp)
            )
            Text(
                text = NilamStrings.trustedBadge(lang),
                color = FbBlue,
                fontWeight = FontWeight.Bold,
                fontSize = if (isSmall) 10.sp else 11.sp
            )
        }
    }
}

@Composable
fun CountdownBadge(
    endTimeMillis: Long,
    clockTick: Long,
    lang: Language,
    modifier: Modifier = Modifier
) {
    val remaining = endTimeMillis - clockTick
    val isUrgent = remaining in 1..300000L // Under 5 mins
    val isEnded = remaining <= 0

    val bgColor = when {
        isEnded -> Color(0xFF65676B)
        isUrgent -> Color(0xFFE41E3F)
        else -> Color.Black.copy(alpha = 0.72f)
    }

    Surface(
        modifier = modifier.clip(RoundedCornerShape(8.dp)),
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = "Countdown",
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = BanglaUtils.formatDuration(remaining, lang),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun NilamTopBar(
    lang: Language,
    isDarkTheme: Boolean,
    unreadNotifCount: Int,
    unreadInboxCount: Int,
    onToggleLang: () -> Unit,
    onToggleTheme: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenInbox: () -> Unit,
    onOpenNotifications: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Facebook-Style App Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "নিলাম",
                        color = FbBlue,
                        fontWeight = FontWeight.Black,
                        fontSize = 28.sp,
                        letterSpacing = (-0.5).sp
                    )
                }

                // Facebook-Style Circular Action Icons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Search icon circle
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onOpenSearch)
                            .testTag("fb_top_search_btn")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Messenger / Inbox circle
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onOpenInbox)
                            .testTag("fb_top_inbox_btn")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
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
                                    imageVector = Icons.Outlined.ChatBubbleOutline,
                                    contentDescription = "Inbox",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Notifications Bell circle
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onOpenNotifications)
                            .testTag("fb_top_notif_btn")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            BadgedBox(
                                badge = {
                                    if (unreadNotifCount > 0) {
                                        Badge(
                                            containerColor = NilamErrorRed,
                                            contentColor = Color.White
                                        ) {
                                            Text(
                                                text = if (lang == Language.BN)
                                                    BanglaUtils.toBanglaDigits(unreadNotifCount.toString())
                                                else
                                                    unreadNotifCount.toString()
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Notifications",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }


                    // Language toggle pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .height(34.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(onClick = onToggleLang)
                            .testTag("lang_toggle_btn")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = if (lang == Language.BN) "English" else "বাংলা",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FbBlue
                            )
                        }
                    }
                }
            }

            // Bottom hairline border like Facebook
            Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
        }
    }
}

@Composable
fun ToastNotificationOverlay(
    message: String?,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = message != null,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
        modifier = modifier
            .fillMaxWidth()
            .padding(14.dp)
            .statusBarsPadding()
    ) {
        if (message != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                color = Color(0xFF242526),
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = FbBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = message,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
