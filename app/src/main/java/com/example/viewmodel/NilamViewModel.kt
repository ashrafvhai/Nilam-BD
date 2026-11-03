package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen {
    HOME, SEARCH, POST_CREATE, INBOX, CHAT_DETAIL, PROFILE
}

class NilamViewModel : ViewModel() {

    // Language selection on first launch
    private val _isLanguageSelected = MutableStateFlow(false)
    val isLanguageSelected: StateFlow<Boolean> = _isLanguageSelected.asStateFlow()

    private val _language = MutableStateFlow(Language.BN)
    val language: StateFlow<Language> = _language.asStateFlow()

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Default to Light Mode as requested ("থেকে থাকবে লাইট")
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _currentUser = MutableStateFlow(SampleData.currentUser)
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _posts = MutableStateFlow<List<AuctionPost>>(SampleData.initialPosts)
    val posts: StateFlow<List<AuctionPost>> = _posts.asStateFlow()

    private val _chats = MutableStateFlow<List<ChatConversation>>(SampleData.initialChats)
    val chats: StateFlow<List<ChatConversation>> = _chats.asStateFlow()

    private val _messages = MutableStateFlow<Map<String, List<ChatMessage>>>(SampleData.initialMessages)
    val messages: StateFlow<Map<String, List<ChatMessage>>> = _messages.asStateFlow()

    // Strictly targeted notifications (3 types: bid on post, bid accepted, outbid)
    private val _notifications = MutableStateFlow<List<AppNotification>>(SampleData.initialNotifications)
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _selectedCategory = MutableStateFlow(Category.ALL)
    val selectedCategory: StateFlow<Category> = _selectedCategory.asStateFlow()

    private val _trustedOnly = MutableStateFlow(false)
    val trustedOnly: StateFlow<Boolean> = _trustedOnly.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.ENDING_SOON)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Transient UI State (dialogs & bottom sheets)
    private val _activeToast = MutableStateFlow<String?>(null)
    val activeToast: StateFlow<String?> = _activeToast.asStateFlow()

    private val _detailPost = MutableStateFlow<AuctionPost?>(null)
    val detailPost: StateFlow<AuctionPost?> = _detailPost.asStateFlow()

    private val _biddingPost = MutableStateFlow<AuctionPost?>(null)
    val biddingPost: StateFlow<AuctionPost?> = _biddingPost.asStateFlow()

    private val _sellerControlPost = MutableStateFlow<AuctionPost?>(null)
    val sellerControlPost: StateFlow<AuctionPost?> = _sellerControlPost.asStateFlow()

    private val _activeChatId = MutableStateFlow<String?>(null)
    val activeChatId: StateFlow<String?> = _activeChatId.asStateFlow()

    // Clock ticker to refresh live countdowns every second
    private val _clockTick = MutableStateFlow(System.currentTimeMillis())
    val clockTick: StateFlow<Long> = _clockTick.asStateFlow()

    init {
        // Simple countdown ticker
        viewModelScope.launch {
            while (isActive) {
                delay(1000)
                _clockTick.value = System.currentTimeMillis()
            }
        }
    }

    // Language selection when first entering the app
    fun selectInitialLanguage(selected: Language) {
        _language.value = selected
        _isLanguageSelected.value = true
    }

    // Navigation
    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun openChat(chatId: String) {
        _activeChatId.value = chatId
        _currentScreen.value = AppScreen.CHAT_DETAIL
    }

    fun closeChat() {
        _activeChatId.value = null
        _currentScreen.value = AppScreen.INBOX
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == Language.BN) Language.EN else Language.BN
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun setCategory(category: Category) {
        _selectedCategory.value = category
    }

    fun setTrustedOnly(trusted: Boolean) {
        _trustedOnly.value = trusted
    }

    fun setSortOption(sort: SortOption) {
        _sortOption.value = sort
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openPostDetail(post: AuctionPost) {
        _detailPost.value = post
    }

    fun closePostDetail() {
        _detailPost.value = null
    }

    fun openBidding(post: AuctionPost) {
        _biddingPost.value = post
    }

    fun closeBidding() {
        _biddingPost.value = null
    }

    fun openSellerControls(post: AuctionPost) {
        _sellerControlPost.value = post
    }

    fun closeSellerControls() {
        _sellerControlPost.value = null
    }

    fun showToast(msg: String) {
        _activeToast.value = msg
        viewModelScope.launch {
            delay(3500)
            if (_activeToast.value == msg) {
                _activeToast.value = null
            }
        }
    }

    // Place a bid (Simplified)
    fun placeBid(postId: String, amount: Long, message: String = "", quickChip: String? = null): Boolean {
        val user = _currentUser.value
        val lang = _language.value

        val post = _posts.value.find { it.id == postId } ?: return false

        // Validate bid amount
        val minAcceptable = maxOf(post.startingPrice, post.currentHighestBid + 500)
        if (amount < minAcceptable) {
            val err = if (lang == Language.BN)
                "বিড অবশ্যই বর্তমান সর্বোচ্চ বিডের চেয়ে বেশি হতে হবে (কমপক্ষে ${BanglaUtils.formatPrice(minAcceptable, lang)})"
            else
                "Bid must exceed current highest (min ${BanglaUtils.formatPrice(minAcceptable, lang)})"
            showToast(err)
            return false
        }

        // Remember previous highest bid to notify if outbid
        val previousHighestBid = post.bids.maxByOrNull { it.amount }

        // Anti-Sniping Check: if bid placed within last 60 seconds of auction
        val now = System.currentTimeMillis()
        val remaining = post.endTimeMillis - now
        var newEndTime = post.endTimeMillis
        var antiSniped = post.antiSnipedCount

        if (remaining in 1..60000L) {
            newEndTime += 120000L // Add 2 minutes
            antiSniped += 1
            showToast(if (lang == Language.BN) "অ্যান্টি-স্নাইপিং সক্রিয়: সময় ২ মিনিট বৃদ্ধি পেয়েছে!" else "Anti-sniping active: 2 mins added!")
        }

        val newBid = Bid(
            id = "bid_${UUID.randomUUID()}",
            postId = postId,
            bidder = user,
            amount = amount,
            timestampMillis = now,
            message = message,
            quickChip = quickChip
        )

        val updatedPost = post.copy(
            currentHighestBid = amount,
            bids = listOf(newBid) + post.bids,
            endTimeMillis = newEndTime,
            antiSnipedCount = antiSniped
        )

        _posts.value = _posts.value.map { if (it.id == postId) updatedPost else it }
        if (_detailPost.value?.id == postId) {
            _detailPost.value = updatedPost
        }

        // 1. SPECIFIC NOTIFICATION: Notify post creator (seller)
        if (post.seller.id != user.id) {
            val sellerNotif = AppNotification(
                id = "notif_bid_${UUID.randomUUID()}",
                titleBn = "আপনার পোস্টে নতুন বিড এসেছে!",
                titleEn = "New bid on your post!",
                bodyBn = "${user.name} আপনার '${post.title}' পোস্টে ${BanglaUtils.formatPrice(amount, lang)} বিড করেছেন।",
                bodyEn = "${user.name} placed a ${BanglaUtils.formatPrice(amount, lang)} bid on your '${post.title}'.",
                timestampMillis = now,
                postId = postId,
                recipientUserId = post.seller.id,
                type = NotificationType.BID
            )
            _notifications.value = listOf(sellerNotif) + _notifications.value
        }

        // 2. SPECIFIC NOTIFICATION: Notify previous highest bidder if outbid
        if (previousHighestBid != null && previousHighestBid.bidder.id != user.id && amount > previousHighestBid.amount) {
            val outbidNotif = AppNotification(
                id = "notif_outbid_${UUID.randomUUID()}",
                titleBn = "⚠️ কেউ আপনার চেয়ে বেশি বিড করেছে!",
                titleEn = "⚠️ You have been outbid!",
                bodyBn = "${user.name} '${post.title}' পণ্যে আপনার চেয়ে বেশি ${BanglaUtils.formatPrice(amount, lang)} বিড করেছেন।",
                bodyEn = "${user.name} outbid you with ${BanglaUtils.formatPrice(amount, lang)} on '${post.title}'.",
                timestampMillis = now,
                postId = postId,
                recipientUserId = previousHighestBid.bidder.id,
                type = NotificationType.OUTBID
            )
            _notifications.value = listOf(outbidNotif) + _notifications.value
        }

        val successMsg = if (lang == Language.BN)
            "আপনার ${BanglaUtils.formatPrice(amount, lang)} বিড সফলভাবে সম্পন্ন হয়েছে!"
        else
            "Your bid of ${BanglaUtils.formatPrice(amount, lang)} placed successfully!"
        showToast(successMsg)

        // Close bidding sheet
        _biddingPost.value = null
        return true
    }

    // Seller controls: accept any bid (Simplified)
    fun acceptBid(postId: String, bidId: String, reason: String? = null) {
        val post = _posts.value.find { it.id == postId } ?: return
        val bid = post.bids.find { it.id == bidId } ?: return
        val lang = _language.value

        val updatedPost = post.copy(
            isClosed = true,
            winningBidId = bidId,
            winnerChoiceReason = reason,
            bids = post.bids.map { if (it.id == bidId) it.copy(isAccepted = true) else it }
        )

        _posts.value = _posts.value.map { if (it.id == postId) updatedPost else it }
        if (_detailPost.value?.id == postId) {
            _detailPost.value = updatedPost
        }

        // Create or find chat conversation
        val existingChat = _chats.value.find { it.postId == postId && it.buyer.id == bid.bidder.id }
        val chatId = existingChat?.id ?: "chat_${UUID.randomUUID()}"

        val newChat = existingChat ?: ChatConversation(
            id = chatId,
            postId = postId,
            postTitle = post.title,
            postPrice = bid.amount,
            postImage = post.images.firstOrNull() ?: "",
            seller = post.seller,
            buyer = bid.bidder,
            lastMessage = if (lang == Language.BN) "🎉 বিড গৃহীত হয়েছে!" else "🎉 Bid accepted!",
            lastMessageTime = System.currentTimeMillis(),
            unreadCount = 1
        )

        if (existingChat == null) {
            _chats.value = listOf(newChat) + _chats.value
        }

        val systemMsg = ChatMessage(
            id = "sys_${UUID.randomUUID()}",
            chatId = chatId,
            senderId = "system",
            senderName = "নিলাম",
            text = if (lang == Language.BN)
                "🎉 বিক্রেতা ${post.seller.name} ${bid.bidder.name}-এর ${BanglaUtils.formatPrice(bid.amount, lang)} বিডটি একসেপ্ট করেছেন।"
            else
                "🎉 Seller ${post.seller.name} accepted ${bid.bidder.name}'s bid of ${BanglaUtils.formatPrice(bid.amount, lang)}.",
            timestampMillis = System.currentTimeMillis(),
            isMine = false,
            isSystem = true
        )

        val chatMsgs = _messages.value[chatId] ?: emptyList()
        _messages.value = _messages.value + (chatId to (chatMsgs + systemMsg))

        // 3. SPECIFIC NOTIFICATION: Notify the winning bidder ONLY
        val winnerNotif = AppNotification(
            id = "notif_win_${UUID.randomUUID()}",
            titleBn = "🎉 আপনার বিডটি একসেপ্ট হয়েছে!",
            titleEn = "🎉 Your bid was accepted!",
            bodyBn = "${post.seller.name} '${post.title}' পণ্যে আপনার ${BanglaUtils.formatPrice(bid.amount, lang)} বিডটি একসেপ্ট করেছেন। চ্যাটে কথা বলুন।",
            bodyEn = "${post.seller.name} accepted your ${BanglaUtils.formatPrice(bid.amount, lang)} bid for '${post.title}'. Chat now.",
            timestampMillis = System.currentTimeMillis(),
            read = false,
            postId = postId,
            chatId = chatId,
            recipientUserId = bid.bidder.id,
            type = NotificationType.WINNER
        )
        _notifications.value = listOf(winnerNotif) + _notifications.value

        _sellerControlPost.value = null
        showToast(if (lang == Language.BN) "বিড একসেপ্ট করা হয়েছে এবং বায়ারকে জানানো হয়েছে!" else "Bid accepted and buyer notified!")
    }

    // Send a message in inbox
    fun sendMessage(chatId: String, text: String) {
        if (text.isBlank()) return
        val current = _messages.value[chatId] ?: emptyList()
        val user = _currentUser.value

        val newMsg = ChatMessage(
            id = "msg_${UUID.randomUUID()}",
            chatId = chatId,
            senderId = user.id,
            senderName = user.name,
            text = text,
            timestampMillis = System.currentTimeMillis(),
            isMine = true
        )

        _messages.value = _messages.value + (chatId to (current + newMsg))

        // Update chat conversation last message
        _chats.value = _chats.value.map {
            if (it.id == chatId) {
                it.copy(lastMessage = text, lastMessageTime = System.currentTimeMillis())
            } else it
        }
    }

    // Create a new auction post
    fun createNewPost(
        title: String,
        description: String,
        category: Category,
        condition: ItemCondition,
        location: String,
        startingPrice: Long,
        durationHours: Int,
        images: List<String>
    ) {
        val now = System.currentTimeMillis()
        val newPost = AuctionPost(
            id = "post_${UUID.randomUUID()}",
            title = title,
            description = description,
            category = category,
            condition = condition,
            location = location,
            startingPrice = startingPrice,
            currentHighestBid = startingPrice,
            endTimeMillis = now + (durationHours * 3600 * 1000L),
            createdAtMillis = now,
            seller = _currentUser.value,
            images = if (images.isNotEmpty()) images else listOf("https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600&auto=format&fit=crop&q=80"),
            bids = emptyList()
        )

        _posts.value = listOf(newPost) + _posts.value
        _currentScreen.value = AppScreen.HOME
        val lang = _language.value
        showToast(if (lang == Language.BN) "আপনার নিলাম সফলভাবে পোস্ট হয়েছে!" else "Auction listed successfully!")
    }

    // AI Suggestion helper for creating posts
    fun generateAiSuggestion(title: String, category: Category): Pair<String, Long> {
        val lower = title.lowercase()
        return when {
            lower.contains("iphone") || lower.contains("আইফোন") -> {
                Pair(
                    "অফিসিয়াল অথোরাইজড শপ থেকে কেনা। শতভাগ ফ্রেশ বডি ও ডিসপ্লে। কোনো ডেন্ট বা স্ক্র্যাচ নেই। ব্যাটারি হেলথ চমৎকার। অরিজিনাল ক্যাবল, বক্স এবং ক্যাশ মেমো সাথে দেওয়া হবে। আগ্রহী বিশ্বস্ত ক্রেতারা দ্রুত বিড করুন।",
                    110000L
                )
            }
            lower.contains("toyota") || lower.contains("গাড়ি") || lower.contains("bike") || lower.contains("বাইক") || lower.contains("r15") -> {
                Pair(
                    "ব্যক্তিগত ব্যবহারের গাড়ি/বাইক, কোনো দুর্ঘটনা ঘটেনি। ইঞ্জিন ফুল ফ্রেশ। সকল কাগজপত্র আপডেট আছে। সরাসরি টেস্ট ড্রাইভ দেখে বিড করতে পারেন।",
                    280000L
                )
            }
            lower.contains("শাড়ি") || lower.contains("saree") || lower.contains("জামদানি") -> {
                Pair(
                    "খাঁটি তাঁতিদের হাতে তৈরি এক্সক্লুসিভ কালেকশন। নিখুঁত ডিজাইনের কাজ এবং দীর্ঘস্থায়ী পাকা রঙ। বিবাহ ও যেকোনো বিশেষ অনুষ্ঠানের জন্য উপযোগী। সম্পূর্ণ অক্ষত এবং নতুন অবস্থায় আছে।",
                    12000L
                )
            }
            lower.contains("laptop") || lower.contains("ল্যাপটপ") || lower.contains("macbook") -> {
                Pair(
                    "অফিসিয়াল কাজের জন্য ব্যবহৃত আল্ট্রা-ফাস্ট ল্যাপটপ। স্ক্রিন ও কিবোর্ড একদম নতুনের মতো। ব্যাটারি ব্যাকআপ চমৎকার। অরিজিনাল ফাস্ট চার্জার ও বক্সের সাথে দেওয়া হবে।",
                    65000L
                )
            }
            else -> {
                Pair(
                    "খুবই যত্নসহকারে ব্যবহৃত এবং ভালো কন্ডিশনের পণ্য। জরুরি প্রয়োজনে সঠিক ক্রেতার কাছে বিক্রি করতে চাই। কোনো অভ্যন্তরীণ সমস্যা নেই। সরাসরি যাচাই করে নিতে পারবেন।",
                    15000L
                )
            }
        }
    }
}
