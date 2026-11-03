package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.model.*
import com.example.ui.components.TrustedBadge
import com.example.ui.theme.*

@Composable
fun BidHistorySheet(
    post: AuctionPost,
    isSeller: Boolean,
    lang: Language,
    onDismiss: () -> Unit,
    onAcceptBid: (bidId: String, reason: String?) -> Unit
) {
    var selectedBidForAcceptance by remember { mutableStateOf<Bid?>(null) }
    var nonHighestReason by remember { mutableStateOf("") }
    var selectedReasonChip by remember { mutableStateOf<String?>(null) }

    val presetReasons = listOf(
        NilamStrings.reasonContactFailed(lang),
        NilamStrings.reasonNearbyLocation(lang),
        NilamStrings.reasonBetterRating(lang)
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = NilamStrings.bidHistoryBtn(lang),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${post.bids.size} ${if (lang == Language.BN) "টি বিডের রেকর্ড" else "bids placed"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)

                // If seller is choosing winner confirmation
                if (selectedBidForAcceptance != null) {
                    val bid = selectedBidForAcceptance!!
                    val isHighest = bid.amount >= post.currentHighestBid

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        color = FbBlueLight,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FbBlue)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Gavel, contentDescription = null, tint = FbBlue)
                                Text(
                                    text = if (lang == Language.BN) "বিড গ্রহণের নিশ্চয়তা" else "Confirm Bid Acceptance",
                                    fontWeight = FontWeight.Bold,
                                    color = FbBlue
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${bid.bidder.name}-এর ${BanglaUtils.formatPrice(bid.amount, lang)} বিডটি গ্রহণ করছেন?",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            if (!isHighest) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = NilamStrings.reasonIfNotHighest(lang),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NilamWarningOrange,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    items(presetReasons) { r ->
                                        val sel = selectedReasonChip == r
                                        Surface(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable {
                                                    selectedReasonChip = if (sel) null else r
                                                    nonHighestReason = if (sel) "" else r
                                                },
                                            color = if (sel) FbBlue else MaterialTheme.colorScheme.surface,
                                            border = if (sel) null else androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
                                        ) {
                                            Text(
                                                text = r,
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                color = if (sel) Color.White else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = nonHighestReason,
                                    onValueChange = { nonHighestReason = it },
                                    placeholder = { Text(if (lang == Language.BN) "অন্য কোনো কারণ..." else "Other reason...") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { selectedBidForAcceptance = null },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(if (lang == Language.BN) "বাতিল" else "Cancel")
                                }
                                Button(
                                    onClick = {
                                        onAcceptBid(bid.id, nonHighestReason.ifBlank { selectedReasonChip })
                                        selectedBidForAcceptance = null
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = FbBlue,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(if (lang == Language.BN) "একসেপ্ট" else "Accept", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // List of Bids
                if (post.bids.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (lang == Language.BN) "এখনও কোনো বিড করা হয়নি" else "No bids yet",
                            color = FbTextSecondary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(post.bids) { bid ->
                            val isHighest = bid.amount >= post.currentHighestBid
                            val isWinning = bid.id == post.winningBidId

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = when {
                                    isWinning -> NilamGoldSoft.copy(alpha = 0.5f)
                                    isHighest -> FbBlueLight
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                },
                                border = if (isWinning || isHighest)
                                    androidx.compose.foundation.BorderStroke(1.dp, if (isWinning) NilamGoldPrimary else FbBlue)
                                else
                                    androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        AsyncImage(
                                            model = bid.bidder.avatar,
                                            contentDescription = bid.bidder.name,
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )

                                        Column {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(
                                                    text = bid.bidder.name,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                if (bid.bidder.isTrusted) {
                                                    TrustedBadge(lang = lang, isSmall = true)
                                                }
                                                if (isHighest) {
                                                    Text(text = "👑", fontSize = 12.sp)
                                                }
                                            }

                                            Text(
                                                text = BanglaUtils.formatRelativeTime(bid.timestampMillis, lang),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )

                                            if (bid.message.isNotBlank()) {
                                                Text(
                                                    text = "\"${bid.message}\"",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    maxLines = 2
                                                )
                                            }
                                        }
                                    }

                                    // Amount and Accept button
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = BanglaUtils.formatPrice(bid.amount, lang),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isHighest || isWinning) FbBlue else MaterialTheme.colorScheme.onSurface
                                        )

                                        if (isWinning) {
                                            Text(
                                                text = if (lang == Language.BN) "🏆 বিজয়ী" else "🏆 Winner",
                                                color = NilamGoldPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        } else if (isSeller && !post.isClosed) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Button(
                                                onClick = { selectedBidForAcceptance = bid },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = FbBlue,
                                                    contentColor = Color.White
                                                ),
                                                shape = RoundedCornerShape(8.dp),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                modifier = Modifier.height(30.dp)
                                            ) {
                                                Text(
                                                    text = NilamStrings.acceptThisBid(lang),
                                                    fontSize = 11.sp,
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

                // Mandatory Note
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "📌 ${NilamStrings.sellerDecisionFinal(lang)}",
                        modifier = Modifier.padding(10.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
