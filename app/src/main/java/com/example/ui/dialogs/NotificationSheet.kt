package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppNotification
import com.example.model.BanglaUtils
import com.example.model.Language
import com.example.model.NotificationType
import com.example.ui.theme.*

@Composable
fun NotificationSheet(
    notifications: List<AppNotification>,
    currentUserId: String,
    lang: Language,
    onDismiss: () -> Unit,
    onSelectNotification: (AppNotification) -> Unit
) {
    // Only show targeted notifications for the current user (Post creator, accepted bidder, or outbid user)
    val userNotifications = notifications.filter {
        it.recipientUserId == null || it.recipientUserId == currentUserId
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.80f),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Facebook-Style Notification Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (lang == Language.BN) "নোটিফিকেশন" else "Notifications",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)

                if (userNotifications.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = FbTextSecondary,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = if (lang == Language.BN) "কোনো নতুন নোটিফিকেশন নেই" else "No new notifications",
                                color = FbTextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(userNotifications, key = { it.id }) { notif ->
                            val icon = when (notif.type) {
                                NotificationType.WINNER -> Icons.Default.EmojiEvents
                                NotificationType.BID -> Icons.Default.Gavel
                                NotificationType.OUTBID -> Icons.Default.TrendingUp
                                NotificationType.WARNING -> Icons.Default.Warning
                                else -> Icons.Default.Notifications
                            }
                            val iconContainer = when (notif.type) {
                                NotificationType.WINNER -> NilamGoldSoft
                                NotificationType.BID -> FbBlueLight
                                NotificationType.OUTBID -> Color(0xFFFFEBEB)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                            val iconTint = when (notif.type) {
                                NotificationType.WINNER -> NilamGoldPrimary
                                NotificationType.BID -> FbBlue
                                NotificationType.OUTBID -> NilamErrorRed
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectNotification(notif) },
                                color = if (notif.read) MaterialTheme.colorScheme.surface else FbBlueLight.copy(alpha = 0.35f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = iconContainer,
                                        modifier = Modifier.size(44.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = null,
                                                tint = iconTint,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (lang == Language.BN) notif.titleBn else notif.titleEn,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = if (lang == Language.BN) notif.bodyBn else notif.bodyEn,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = BanglaUtils.formatRelativeTime(notif.timestampMillis, lang),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (notif.read) FbTextSecondary else FbBlue,
                                            fontSize = 11.sp,
                                            fontWeight = if (notif.read) FontWeight.Normal else FontWeight.SemiBold
                                        )
                                    }

                                    if (!notif.read) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(FbBlue)
                                        )
                                    }
                                }
                            }

                            Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                        }
                    }
                }
            }
        }
    }
}
