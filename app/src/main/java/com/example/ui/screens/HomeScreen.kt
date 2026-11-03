package com.example.ui.screens

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
import androidx.compose.material.icons.outlined.*
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
import com.example.ui.components.CountdownBadge
import com.example.ui.components.TrustedBadge
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    posts: List<AuctionPost>,
    selectedCategory: Category,
    trustedOnly: Boolean,
    sortOption: SortOption,
    clockTick: Long,
    lang: Language,
    currentUser: User,
    onSelectCategory: (Category) -> Unit,
    onToggleTrustedOnly: (Boolean) -> Unit,
    onSelectSortOption: (SortOption) -> Unit,
    onOpenPostDetail: (AuctionPost) -> Unit,
    onOpenBidding: (AuctionPost) -> Unit,
    onOpenSellerControls: (AuctionPost) -> Unit,
    onNavigateToCreatePost: () -> Unit,
    onSharePost: (AuctionPost) -> Unit,
    modifier: Modifier = Modifier
) {
    // Filter and sort posts
    val filteredPosts = remember(posts, selectedCategory, trustedOnly, sortOption) {
        posts.filter { post ->
            val matchCategory = selectedCategory == Category.ALL || post.category == selectedCategory
            val matchTrusted = !trustedOnly || post.seller.isTrusted
            matchCategory && matchTrusted
        }.sortedWith { a, b ->
            val boostA = (if (a.isBoosted) 2 else 0) + (if (a.seller.isTrusted) 1 else 0)
            val boostB = (if (b.isBoosted) 2 else 0) + (if (b.seller.isTrusted) 1 else 0)

            if (boostA != boostB) {
                boostB.compareTo(boostA)
            } else {
                when (sortOption) {
                    SortOption.ENDING_SOON -> a.endTimeMillis.compareTo(b.endTimeMillis)
                    SortOption.NEWEST -> b.createdAtMillis.compareTo(a.createdAtMillis)
                    SortOption.HIGHEST_BID -> b.currentHighestBid.compareTo(a.currentHighestBid)
                }
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Facebook-Style "Create Auction / What's on your mind?" Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 0.5.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onNavigateToCreatePost)
                            .testTag("fb_create_post_bar"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsyncImage(
                            model = currentUser.avatar,
                            contentDescription = currentUser.name,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = if (lang == Language.BN)
                                        "${currentUser.name}, কী বিক্রি করতে চান? নিলাম শুরু করুন..."
                                    else
                                        "What do you want to auction, ${currentUser.name}?",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        IconButton(onClick = onNavigateToCreatePost) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = "Add Photos",
                                tint = FbBlue,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }
        }

        // 2. Facebook-Style Horizontal Category Rail
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 0.5.dp
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(Category.values()) { category ->
                            val isSelected = category == selectedCategory
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { onSelectCategory(category) }
                                    .testTag("filter_chip_${category.name}"),
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) FbBlue else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(category.icon, fontSize = 13.sp)
                                    Text(
                                        text = if (lang == Language.BN) category.labelBn else category.labelEn,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 13.sp,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)

                    // Secondary Filters: Trusted Only + Sort Options
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Trusted Switch
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Switch(
                                checked = trustedOnly,
                                onCheckedChange = onToggleTrustedOnly,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = FbBlue,
                                    checkedTrackColor = FbBlueLight
                                ),
                                modifier = Modifier.size(width = 44.dp, height = 24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = NilamStrings.trustedOnly(lang),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Sort Chips
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            SortOption.values().forEach { opt ->
                                val isSel = opt == sortOption
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onSelectSortOption(opt) },
                                    color = if (isSel) FbBlueLight else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = if (lang == Language.BN) opt.labelBn else opt.labelEn,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                        fontSize = 11.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSel) FbBlue else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Facebook Post Cards Feed
        if (filteredPosts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = FbTextSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = if (lang == Language.BN) "কোনো নিলাম পোস্ট নেই" else "No auctions found",
                            color = FbTextSecondary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        } else {
            items(filteredPosts, key = { it.id }) { post ->
                FacebookAuctionCard(
                    post = post,
                    clockTick = clockTick,
                    lang = lang,
                    isCurrentUserSeller = post.seller.id == currentUser.id,
                    onOpenDetail = { onOpenPostDetail(post) },
                    onPlaceBid = { onOpenBidding(post) },
                    onSellerControls = { onOpenSellerControls(post) },
                    onShare = { onSharePost(post) }
                )
            }
        }
    }
}

@Composable
fun FacebookAuctionCard(
    post: AuctionPost,
    clockTick: Long,
    lang: Language,
    isCurrentUserSeller: Boolean,
    onOpenDetail: () -> Unit,
    onPlaceBid: () -> Unit,
    onSellerControls: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .testTag("post_card_${post.id}"),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 0.5.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // --- Facebook Post Header ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AsyncImage(
                        model = post.seller.avatar,
                        contentDescription = post.seller.name,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = post.seller.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (post.seller.isTrusted) {
                                TrustedBadge(lang = lang, isSmall = true)
                            }
                        }

                        // Subtitle: Time + Facebook Globe Public Icon + Location
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = BanglaUtils.formatRelativeTime(post.createdAtMillis, lang),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                            Text(text = "·", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = "Public",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(text = "·", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = post.location,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Three dots / Boost badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (post.isBoosted) {
                        Surface(
                            color = FbBlueLight,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (lang == Language.BN) "⚡ স্পনসর্ড" else "⚡ Sponsored",
                                color = FbBlue,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    IconButton(onClick = onOpenDetail) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Options",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // --- Post Description & Title (Status Text) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = post.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // --- Product Image Media with Countdown Overlay ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.Black)
                    .clickable(onClick = onOpenDetail)
            ) {
                AsyncImage(
                    model = post.images.firstOrNull() ?: "",
                    contentDescription = post.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Floating Tags: Condition on left, Countdown on right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .align(Alignment.TopStart),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.72f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (lang == Language.BN) post.condition.labelBn else post.condition.labelEn,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    CountdownBadge(
                        endTimeMillis = post.endTimeMillis,
                        clockTick = clockTick,
                        lang = lang
                    )
                }

                // If auction is closed
                if (post.isClosed) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.65f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(10.dp),
                            shadowElevation = 4.dp
                        ) {
                            Text(
                                text = if (lang == Language.BN) "🏆 নিলাম সম্পন্ন হয়েছে" else "🏆 Auction Completed",
                                color = FbBlue,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // --- Facebook Reaction & Bid Stats Row ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Reaction / Bid icons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = FbBlue,
                        modifier = Modifier.size(18.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Gavel,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                    Text(
                        text = NilamStrings.totalBids(post.bids.size, lang),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Right: Current Highest Bid vs Start Price
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${NilamStrings.currentHighestBid(lang)}:",
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
            }

            // Hairline Divider
            Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)

            // --- Facebook 3 Action Buttons Row: [🔨 বিড করুন] [💬 বিড হিস্ট্রি] [↗ শেয়ার] ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Button 1: Bid or Seller Winner Control
                if (isCurrentUserSeller) {
                    TextButton(
                        onClick = onSellerControls,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("seller_control_btn_${post.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = null,
                            tint = FbBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (lang == Language.BN) "বিজয়ী বাছুন" else "Winner",
                            fontWeight = FontWeight.Bold,
                            color = FbBlue,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    TextButton(
                        onClick = onPlaceBid,
                        enabled = !post.isClosed && post.endTimeMillis > clockTick,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("place_bid_btn_${post.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = null,
                            tint = if (!post.isClosed) FbBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = NilamStrings.placeBidBtn(lang),
                            fontWeight = FontWeight.Bold,
                            color = if (!post.isClosed) FbBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    }
                }

                // Button 2: Bid History (Comment icon style)
                TextButton(
                    onClick = onOpenDetail,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("bid_history_btn_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = NilamStrings.bidHistoryBtn(lang),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }

                // Button 3: Share
                TextButton(
                    onClick = onShare,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (lang == Language.BN) "শেয়ার" else "Share",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
            }

            // --- Facebook Comment-Stream Style: Recent Bids Preview directly in Feed ---
            if (post.bids.isNotEmpty()) {
                Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Show top 2 latest bids as Facebook comment bubbles
                    post.bids.take(2).forEach { bid ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            AsyncImage(
                                model = bid.bidder.avatar,
                                contentDescription = bid.bidder.name,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            // Facebook Comment Bubble
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f, fill = false)
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = bid.bidder.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (bid.bidder.isTrusted) {
                                            TrustedBadge(lang = lang, isSmall = true)
                                        }
                                        Text(
                                            text = "· ${BanglaUtils.formatPrice(bid.amount, lang)}",
                                            fontWeight = FontWeight.Bold,
                                            color = FbBlue,
                                            fontSize = 12.sp
                                        )
                                    }

                                    if (bid.message.isNotBlank()) {
                                        Text(
                                            text = bid.message,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (post.bids.size > 2) {
                        Text(
                            text = if (lang == Language.BN)
                                "আরও ${BanglaUtils.toBanglaDigits((post.bids.size - 2).toString())}টি বিড ও বিবরণ দেখুন..."
                            else
                                "View ${post.bids.size - 2} more bids...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clickable(onClick = onOpenDetail)
                                .padding(vertical = 2.dp)
                        )
                    }
                }
            }

            // Mandatory footer note
            Text(
                text = "📌 ${NilamStrings.sellerDecisionFinal(lang)}",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}
