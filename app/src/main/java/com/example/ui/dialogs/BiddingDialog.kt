package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun BiddingDialog(
    post: AuctionPost,
    lang: Language,
    onDismiss: () -> Unit,
    onSubmitBid: (amount: Long, message: String, quickChip: String?) -> Unit
) {
    val minBid = maxOf(post.startingPrice, post.currentHighestBid + 500)
    var bidAmountText by remember { mutableStateOf((post.currentHighestBid + 1000).toString()) }
    var userMessage by remember { mutableStateOf("") }
    var selectedQuickChip by remember { mutableStateOf<String?>(null) }
    var agreedToCommitment by remember { mutableStateOf(false) }

    val quickChips = listOf(
        "পণ্যটা আসল তো?",
        "ডেলিভারি হবে?",
        "ক্যাশ অন ডেলিভারি দেওয়া যাবে?",
        "লোকেশন কোথায় পাব?"
    )

    val quickIncrements = listOf(500L, 1000L, 2000L, 5000L)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header with close icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = null,
                            tint = FbBlue
                        )
                        Column {
                            Text(
                                text = NilamStrings.placeBidBtn(lang),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = post.title,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Current Highest & Minimum Next Bid Info Box
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = NilamStrings.currentHighestBid(lang),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = BanglaUtils.formatPrice(post.currentHighestBid, lang),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = FbBlue
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (lang == Language.BN) "কমপক্ষে বিড" else "Min next bid",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = BanglaUtils.formatPrice(minBid, lang),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Increment Chips
                Text(
                    text = if (lang == Language.BN) "দ্রুত পরিমাণ বৃদ্ধি:" else "Quick increment:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickIncrements.forEach { inc ->
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    val current = bidAmountText.toLongOrNull() ?: post.currentHighestBid
                                    bidAmountText = (current + inc).toString()
                                },
                            color = FbBlueLight,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "+${BanglaUtils.formatPrice(inc, lang)}",
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FbBlue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bid Input Field
                OutlinedTextField(
                    value = bidAmountText,
                    onValueChange = { bidAmountText = it.filter { ch -> ch.isDigit() } },
                    label = { Text(NilamStrings.enterBidAmount(lang)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_bid_amount"),
                    singleLine = true,
                    prefix = { Text("৳ ", color = FbBlue, fontWeight = FontWeight.Bold) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Optional Message Field
                OutlinedTextField(
                    value = userMessage,
                    onValueChange = { if (it.length <= 100) userMessage = it },
                    label = { Text(NilamStrings.optionalMessage(lang)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_bid_message"),
                    maxLines = 2,
                    supportingText = {
                        Text(
                            text = "${userMessage.length}/100",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Quick question chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(quickChips) { chip ->
                        val isSelected = selectedQuickChip == chip
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    selectedQuickChip = if (isSelected) null else chip
                                    if (userMessage.isBlank() && !isSelected) {
                                        userMessage = chip
                                    }
                                },
                            color = if (isSelected) FbBlueLight else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, FbBlue) else null
                        ) {
                            Text(
                                text = chip,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                color = if (isSelected) FbBlue else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Mandatory Commitment Checkbox: "বিড মানে কেনার প্রতিশ্রুতি"
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { agreedToCommitment = !agreedToCommitment },
                    color = if (agreedToCommitment) FbBlueLight.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (agreedToCommitment) FbBlue else MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = agreedToCommitment,
                            onCheckedChange = { agreedToCommitment = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = FbBlue,
                                checkmarkColor = Color.White
                            ),
                            modifier = Modifier.testTag("checkbox_bid_commitment")
                        )
                        Column {
                            Text(
                                text = NilamStrings.bidCommitmentTitle(lang),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = NilamStrings.bidCommitmentDesc(lang),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Confirm Bid Button (Facebook Blue style)
                Button(
                    onClick = {
                        val amount = bidAmountText.toLongOrNull() ?: 0L
                        onSubmitBid(amount, userMessage, selectedQuickChip)
                    },
                    enabled = agreedToCommitment && (bidAmountText.toLongOrNull() ?: 0L) >= minBid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_bid_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FbBlue,
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFE4E6EB),
                        disabledContentColor = FbTextSecondary
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = NilamStrings.confirmBid(lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
