package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
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
import coil.compose.AsyncImage
import com.example.model.*
import com.example.ui.components.CountdownBadge
import com.example.ui.components.TrustedBadge
import com.example.ui.theme.*

@Composable
fun SearchScreen(
    posts: List<AuctionPost>,
    lang: Language,
    clockTick: Long,
    onOpenPostDetail: (AuctionPost) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf(Category.ALL) }

    val filtered = remember(posts, query, selectedCat) {
        posts.filter { post ->
            val matchQuery = query.isBlank() ||
                    post.title.contains(query, ignoreCase = true) ||
                    post.description.contains(query, ignoreCase = true) ||
                    post.location.contains(query, ignoreCase = true)
            val matchCat = selectedCat == Category.ALL || post.category == selectedCat
            matchQuery && matchCat
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(14.dp)
            .padding(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Search TextField
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text(if (lang == Language.BN) "পণ্য, গাড়ী বা লোকেশন খুঁজুন..." else "Search items, cars, locations...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = NilamGoldBright) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextGrayLight)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_input_field"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NilamGoldBright,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        // Categories Row
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Category.values()) { cat ->
                val isSel = cat == selectedCat
                FilterChip(
                    selected = isSel,
                    onClick = { selectedCat = cat },
                    label = {
                        Text(
                            text = "${cat.icon} " + (if (lang == Language.BN) cat.labelBn else cat.labelEn),
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NilamGoldPrimary,
                        selectedLabelColor = Color.Black
                    )
                )
            }
        }

        Text(
            text = "${filtered.size} ${if (lang == Language.BN) "টি নিলাম পাওয়া গেছে" else "auctions found"}",
            style = MaterialTheme.typography.labelMedium,
            color = TextGrayLight
        )

        // Results
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtered, key = { it.id }) { post ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenPostDetail(post) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AsyncImage(
                                model = post.images.firstOrNull() ?: "",
                                contentDescription = post.title,
                                modifier = Modifier
                                    .size(75.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = post.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                }

                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = post.seller.name, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    if (post.seller.isTrusted) {
                                        TrustedBadge(lang = lang, isSmall = true)
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = BanglaUtils.formatPrice(post.currentHighestBid, lang),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = FbBlue
                                    )

                                    CountdownBadge(

                                    endTimeMillis = post.endTimeMillis,
                                    clockTick = clockTick,
                                    lang = lang
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
