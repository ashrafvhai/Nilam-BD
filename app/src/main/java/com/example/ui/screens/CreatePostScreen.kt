package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreatePostScreen(
    lang: Language,
    onPublishPost: (
        title: String,
        description: String,
        category: Category,
        condition: ItemCondition,
        location: String,
        startingPrice: Long,
        durationHours: Int,
        images: List<String>
    ) -> Unit,
    onAiSuggest: (title: String, category: Category) -> Pair<String, Long>,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category.MOBILE) }
    var selectedCondition by remember { mutableStateOf(ItemCondition.LIKE_NEW) }
    var location by remember { mutableStateOf("ধানমন্ডি, ঢাকা") }
    var startingPriceText by remember { mutableStateOf("") }
    var durationHours by remember { mutableStateOf(24) }
    var isAiGenerating by remember { mutableStateOf(false) }

    val presetImages = listOf(
        "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=600&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80"
    )
    var selectedImages by remember { mutableStateOf(listOf(presetImages.first())) }

    val popularLocations = listOf(
        "ধানমন্ডি, ঢাকা",
        "গুলশান, ঢাকা",
        "বনানী, ঢাকা",
        "উত্তরা, ঢাকা",
        "মিরপুর, ঢাকা",
        "চকবাজার, চট্টগ্রাম",
        "জিন্দাবাজার, সিলেট"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
            .padding(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Facebook-Style Top Bar for Post Creation
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onCancel, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                    Text(
                        text = if (lang == Language.BN) "নতুন নিলাম পোস্ট করুন" else "Create Auction Post",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    color = FbBlueLight,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (lang == Language.BN) "বিনামূল্যে (Free)" else "Free Listing",
                        color = FbBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Image Selection Preview
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (lang == Language.BN) "পণ্যের ছবি নির্বাচন করুন:" else "Select Photos:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(presetImages) { imgUrl ->
                        val isSelected = selectedImages.contains(imgUrl)
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 0.5.dp,
                                    color = if (isSelected) FbBlue else MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    selectedImages = if (isSelected) {
                                        if (selectedImages.size > 1) selectedImages - imgUrl else selectedImages
                                    } else {
                                        selectedImages + imgUrl
                                    }
                                }
                        ) {
                            AsyncImage(
                                model = imgUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = FbBlue,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .padding(2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Title Field with AI Suggest Button
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (lang == Language.BN) "পণ্যের নাম (Title)" else "Product Title") },
                    placeholder = { Text(if (lang == Language.BN) "যেমন: iPhone 14 Pro 128GB" else "e.g., iPhone 14 Pro 128GB") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_post_title"),
                    singleLine = true
                )

                // AI Suggest Button (Facebook Blue style)
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            isAiGenerating = true
                            coroutineScope.launch {
                                delay(1000)
                                val suggestion = onAiSuggest(title, selectedCategory)
                                description = suggestion.first
                                if (startingPriceText.isBlank()) {
                                    startingPriceText = suggestion.second.toString()
                                }
                                isAiGenerating = false
                            }
                        }
                    },
                    enabled = title.isNotBlank() && !isAiGenerating,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ai_suggest_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FbBlueLight,
                        contentColor = FbBlue
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isAiGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = FbBlue,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(NilamStrings.aiGenerating(lang), fontSize = 13.sp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(NilamStrings.aiSuggestBtn(lang), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                // Description Field
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(if (lang == Language.BN) "বিস্তারিত বিবরণ" else "Description") },
                    placeholder = { Text(if (lang == Language.BN) "পণ্যের অবস্থা, ওয়ারেন্টি ইত্যাদি লিখুন..." else "Details, condition, warranty...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("input_post_desc"),
                    maxLines = 4
                )
            }
        }

        // Category & Condition
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = if (lang == Language.BN) "ক্যাটেগরি নির্বাচন:" else "Category:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Category.values().filter { it != Category.ALL }) { cat ->
                        val isSel = cat == selectedCategory
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedCategory = cat },
                            label = {
                                Text(
                                    text = "${cat.icon} " + (if (lang == Language.BN) cat.labelBn else cat.labelEn),
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FbBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)

                Text(
                    text = if (lang == Language.BN) "পণ্যের অবস্থা (Condition):" else "Condition:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ItemCondition.values().forEach { cond ->
                        val isSel = cond == selectedCondition
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedCondition = cond },
                            color = if (isSel) FbBlueLight else MaterialTheme.colorScheme.surfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) FbBlue else MaterialTheme.colorScheme.outline)
                        ) {
                            Text(
                                text = if (lang == Language.BN) cond.labelBn.split(" ").first() else cond.labelEn,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) FbBlue else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Location & Price
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text(if (lang == Language.BN) "লোকেশন" else "Location") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(popularLocations) { loc ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { location = loc },
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = loc,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)

                OutlinedTextField(
                    value = startingPriceText,
                    onValueChange = { startingPriceText = it.filter { ch -> ch.isDigit() } },
                    label = { Text(NilamStrings.startPrice(lang)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    prefix = { Text("৳ ", color = FbBlue, fontWeight = FontWeight.Bold) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_start_price"),
                    singleLine = true
                )
            }
        }

        // Auction Duration
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (lang == Language.BN) "নিলামের সময়কাল:" else "Auction Duration:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val durations = listOf(12 to "১২ ঘণ্টা", 24 to "২৪ ঘণ্টা", 72 to "৩ দিন", 168 to "৭ দিন")
                    durations.forEach { (hrs, label) ->
                        val isSel = durationHours == hrs
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { durationHours = hrs },
                            color = if (isSel) FbBlue else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = if (lang == Language.BN) label else "${hrs}h",
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Note: Seller decision is final
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = FbBlueLight.copy(alpha = 0.5f),
            border = androidx.compose.foundation.BorderStroke(0.5.dp, FbBlue)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = FbBlue)
                Text(
                    text = NilamStrings.sellerDecisionFinal(lang) + " — " + (if (lang == Language.BN) "বিক্রেতা নিজের পছন্দের যে কাউকে বিজয়ী নির্বাচন করতে পারেন।" else "The seller may choose any bidder as the winner."),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Publish Button (Facebook Blue style)
        Button(
            onClick = {
                val price = startingPriceText.toLongOrNull() ?: 1000L
                onPublishPost(
                    title,
                    description,
                    selectedCategory,
                    selectedCondition,
                    location,
                    price,
                    durationHours,
                    selectedImages
                )
            },
            enabled = title.isNotBlank() && description.isNotBlank() && startingPriceText.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("publish_post_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = FbBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Publish, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (lang == Language.BN) "নিলাম পোস্ট করুন (বিনামূল্যে)" else "Publish Auction (Free)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}
