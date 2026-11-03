package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.*
import com.example.ui.components.TrustedBadge
import com.example.ui.theme.*

@Composable
fun InboxScreen(
    chats: List<ChatConversation>,
    lang: Language,
    currentUserId: String,
    onOpenChat: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Facebook-Style Inbox Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 0.5.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = NilamStrings.inbox(lang),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${chats.size} ${if (lang == Language.BN) "টি চ্যাট" else "chats"}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (chats.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = null,
                        tint = FbTextSecondary,
                        modifier = Modifier.size(56.dp)
                    )
                    Text(
                        text = if (lang == Language.BN) "কোনো সক্রিয় চ্যাট নেই" else "No active chats",
                        style = MaterialTheme.typography.bodyLarge,
                        color = FbTextSecondary
                    )
                    Text(
                        text = if (lang == Language.BN)
                            "বিড একসেপ্ট হলে ক্রেতা ও বিক্রেতার মধ্যে সরাসরি চ্যাট শুরু হবে।"
                        else
                            "Chats open automatically once a bid is accepted.",
                        style = MaterialTheme.typography.bodySmall,
                        color = FbTextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chats, key = { it.id }) { chat ->
                    val otherPerson = if (chat.seller.id == currentUserId) chat.buyer else chat.seller

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenChat(chat.id) }
                            .testTag("chat_item_${chat.id}"),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Avatar of other person
                            Box {
                                AsyncImage(
                                    model = otherPerson.avatar,
                                    contentDescription = otherPerson.name,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                // Online dot
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(NilamSuccessGreen)
                                        .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape)
                                        .align(Alignment.BottomEnd)
                                )
                            }

                            // Conversation info
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = otherPerson.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (otherPerson.isTrusted) {
                                            TrustedBadge(lang = lang, isSmall = true)
                                        }
                                    }
                                    Text(
                                        text = BanglaUtils.formatRelativeTime(chat.lastMessageTime, lang),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                // Product tag badge
                                Text(
                                    text = "📦 ${chat.postTitle} (${BanglaUtils.formatPrice(chat.postPrice, lang)})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FbBlue,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = chat.lastMessage,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // Unread indicator
                            if (chat.unreadCount > 0) {
                                Surface(
                                    shape = CircleShape,
                                    color = FbBlue,
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = chat.unreadCount.toString(),
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatDetailScreen(
    chat: ChatConversation,
    messages: List<ChatMessage>,
    lang: Language,
    currentUserId: String,
    onSendMessage: (String) -> Unit,
    onConfirmDeal: (isSeller: Boolean) -> Unit,
    onOpenReportDialog: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSeller = chat.seller.id == currentUserId
    val otherUser = if (isSeller) chat.buyer else chat.seller
    var inputMessage by remember { mutableStateOf("") }

    val dealIsComplete = chat.dealCompletedBuyer && chat.dealCompletedSeller
    val myDealConfirmed = if (isSeller) chat.dealCompletedSeller else chat.dealCompletedBuyer

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Chat Screen Top Bar (Messenger style)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = FbBlue
                    )
                }

                AsyncImage(
                    model = otherUser.avatar,
                    contentDescription = otherUser.name,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = otherUser.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (otherUser.isTrusted) {
                            TrustedBadge(lang = lang, isSmall = true)
                        }
                    }
                    Text(
                        text = if (isSeller) "বায়ার (Buyer)" else "বিক্রেতা (Seller)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // If seller and deal not completed yet: show Report Buyer option
                if (isSeller && !dealIsComplete) {
                    IconButton(onClick = onOpenReportDialog) {
                        Icon(
                            imageVector = Icons.Default.ReportProblem,
                            contentDescription = "Report",
                            tint = NilamErrorRed
                        )
                    }
                }
            }
        }

        // Pinned Product Header Card at Top (Facebook Marketplace style)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, FbBlue.copy(alpha = 0.5f)),
            shadowElevation = 0.5.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AsyncImage(
                    model = chat.postImage,
                    contentDescription = chat.postTitle,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chat.postTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${if (lang == Language.BN) "চুক্তি মূল্য:" else "Price:"} ${BanglaUtils.formatPrice(chat.postPrice, lang)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = FbBlue,
                        fontWeight = FontWeight.Bold
                    )
                }

                // "Deal Completed" (বিক্রি সম্পন্ন) Action Button
                if (dealIsComplete) {
                    Surface(
                        color = NilamSuccessGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (lang == Language.BN) "✓ সম্পন্ন" else "✓ Done",
                            color = NilamSuccessGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = { onConfirmDeal(isSeller) },
                        enabled = !myDealConfirmed,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (myDealConfirmed) MaterialTheme.colorScheme.surfaceVariant else NilamSuccessGreen,
                            contentColor = if (myDealConfirmed) MaterialTheme.colorScheme.onSurfaceVariant else Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("deal_complete_btn")
                    ) {
                        Text(
                            text = if (myDealConfirmed)
                                (if (lang == Language.BN) "অনুমোদিত" else "Waiting")
                            else
                                NilamStrings.dealCompleted(lang),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Messages List (Facebook Messenger Style)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            reverseLayout = false,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                if (msg.isSystem) {
                    // System Notification Message Box
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        color = FbBlueLight,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = msg.text,
                            color = FbBlueDark,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(10.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    val isMe = msg.isMine
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isMe) 16.dp else 4.dp,
                                bottomEnd = if (isMe) 4.dp else 16.dp
                            ),
                            color = if (isMe) FbBlue else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = msg.text,
                                color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = BanglaUtils.formatRelativeTime(msg.timestampMillis, lang),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }

        // Message Input Row
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputMessage,
                    onValueChange = { inputMessage = it },
                    placeholder = { Text(NilamStrings.typeMessage(lang)) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_text"),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )

                IconButton(
                    onClick = {
                        if (inputMessage.isNotBlank()) {
                            onSendMessage(inputMessage)
                            inputMessage = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("send_msg_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = FbBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
