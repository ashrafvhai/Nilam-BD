package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.Language
import com.example.ui.components.NilamBottomNavBar
import com.example.ui.components.NilamTopBar
import com.example.ui.components.ToastNotificationOverlay
import com.example.ui.dialogs.*
import com.example.ui.screens.*
import com.example.ui.theme.NilamTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.NilamViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NilamApp()
        }
    }
}

@Composable
fun NilamApp(viewModel: NilamViewModel = viewModel()) {
    val isLanguageSelected by viewModel.isLanguageSelected.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val chats by viewModel.chats.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val trustedOnly by viewModel.trustedOnly.collectAsStateWithLifecycle()
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()
    val clockTick by viewModel.clockTick.collectAsStateWithLifecycle()
    val activeToast by viewModel.activeToast.collectAsStateWithLifecycle()

    val detailPost by viewModel.detailPost.collectAsStateWithLifecycle()
    val biddingPost by viewModel.biddingPost.collectAsStateWithLifecycle()
    val sellerControlPost by viewModel.sellerControlPost.collectAsStateWithLifecycle()
    val activeChatId by viewModel.activeChatId.collectAsStateWithLifecycle()

    var showNotificationSheet by remember { mutableStateOf(false) }

    val unreadNotifs = remember(notifications) { notifications.count { !it.read } }
    val unreadChats = remember(chats) { chats.sumOf { it.unreadCount } }

    // First time entry: Must select language first!
    if (!isLanguageSelected) {
        NilamTheme(darkTheme = isDarkTheme) {
            LanguageSelectionScreen(
                onLanguageConfirmed = { chosenLang ->
                    viewModel.selectInitialLanguage(chosenLang)
                }
            )
        }
        return
    }

    // Android Hardware Back Navigation Handler
    BackHandler(enabled = currentScreen != AppScreen.HOME || detailPost != null || biddingPost != null || showNotificationSheet) {
        when {
            showNotificationSheet -> showNotificationSheet = false
            biddingPost != null -> viewModel.closeBidding()
            detailPost != null -> viewModel.closePostDetail()
            currentScreen == AppScreen.CHAT_DETAIL -> viewModel.closeChat()
            currentScreen != AppScreen.HOME -> viewModel.navigateTo(AppScreen.HOME)
        }
    }

    NilamTheme(darkTheme = isDarkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                NilamTopBar(
                    lang = language,
                    isDarkTheme = isDarkTheme,
                    unreadNotifCount = unreadNotifs,
                    unreadInboxCount = unreadChats,
                    onToggleLang = { viewModel.toggleLanguage() },
                    onToggleTheme = { viewModel.toggleTheme() },
                    onOpenSearch = { viewModel.navigateTo(AppScreen.SEARCH) },
                    onOpenInbox = { viewModel.navigateTo(AppScreen.INBOX) },
                    onOpenNotifications = { showNotificationSheet = true }
                )
            },
            bottomBar = {
                if (currentScreen != AppScreen.CHAT_DETAIL) {
                    NilamBottomNavBar(
                        currentScreen = currentScreen,
                        lang = language,
                        unreadInboxCount = unreadChats,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Screen content
                when (currentScreen) {
                    AppScreen.HOME -> {
                        HomeScreen(
                            posts = posts,
                            selectedCategory = selectedCategory,
                            trustedOnly = trustedOnly,
                            sortOption = sortOption,
                            clockTick = clockTick,
                            lang = language,
                            currentUser = currentUser,
                            onSelectCategory = { viewModel.setCategory(it) },
                            onToggleTrustedOnly = { viewModel.setTrustedOnly(it) },
                            onSelectSortOption = { viewModel.setSortOption(it) },
                            onOpenPostDetail = { viewModel.openPostDetail(it) },
                            onOpenBidding = { viewModel.openBidding(it) },
                            onOpenSellerControls = { viewModel.openSellerControls(it) },
                            onNavigateToCreatePost = { viewModel.navigateTo(AppScreen.POST_CREATE) },
                            onSharePost = {
                                viewModel.showToast(if (language == Language.BN) "পোস্টের লিংক কপি করা হয়েছে!" else "Post link copied!")
                            }
                        )
                    }

                    AppScreen.SEARCH -> {
                        SearchScreen(
                            posts = posts,
                            lang = language,
                            clockTick = clockTick,
                            onOpenPostDetail = { viewModel.openPostDetail(it) }
                        )
                    }

                    AppScreen.POST_CREATE -> {
                        CreatePostScreen(
                            lang = language,
                            onPublishPost = { title, desc, cat, cond, loc, price, dur, imgs ->
                                viewModel.createNewPost(title, desc, cat, cond, loc, price, dur, imgs)
                            },
                            onAiSuggest = { title, cat ->
                                viewModel.generateAiSuggestion(title, cat)
                            },
                            onCancel = { viewModel.navigateTo(AppScreen.HOME) }
                        )
                    }

                    AppScreen.INBOX -> {
                        InboxScreen(
                            chats = chats,
                            lang = language,
                            currentUserId = currentUser.id,
                            onOpenChat = { viewModel.openChat(it) }
                        )
                    }

                    AppScreen.CHAT_DETAIL -> {
                        val activeChat = chats.find { it.id == activeChatId }
                        if (activeChat != null) {
                            val activeMessages = messages[activeChat.id] ?: emptyList()
                            ChatDetailScreen(
                                chat = activeChat,
                                messages = activeMessages,
                                lang = language,
                                currentUserId = currentUser.id,
                                onSendMessage = { viewModel.sendMessage(activeChat.id, it) },
                                onConfirmDeal = { _ -> },
                                onOpenReportDialog = { },
                                onBack = { viewModel.closeChat() }
                            )
                        } else {
                            viewModel.closeChat()
                        }
                    }

                    AppScreen.PROFILE -> {
                        ProfileScreen(
                            user = currentUser,
                            posts = posts,
                            lang = language,
                            isDarkTheme = isDarkTheme,
                            onToggleLang = { viewModel.toggleLanguage() },
                            onToggleTheme = { viewModel.toggleTheme() },
                            onOpenPostDetail = { viewModel.openPostDetail(it) }
                        )
                    }
                }

                // Floating Toast Notification Overlay
                ToastNotificationOverlay(message = activeToast)
            }
        }

        // Active Dialogs / Bottom Sheets
        if (biddingPost != null) {
            BiddingDialog(
                post = biddingPost!!,
                lang = language,
                onDismiss = { viewModel.closeBidding() },
                onSubmitBid = { amount, message, chip ->
                    viewModel.placeBid(biddingPost!!.id, amount, message, chip)
                }
            )
        }

        if (detailPost != null) {
            BidHistorySheet(
                post = detailPost!!,
                isSeller = detailPost!!.seller.id == currentUser.id,
                lang = language,
                onDismiss = { viewModel.closePostDetail() },
                onAcceptBid = { bidId, reason ->
                    viewModel.acceptBid(detailPost!!.id, bidId, reason)
                }
            )
        }

        if (sellerControlPost != null) {
            BidHistorySheet(
                post = sellerControlPost!!,
                isSeller = true,
                lang = language,
                onDismiss = { viewModel.closeSellerControls() },
                onAcceptBid = { bidId, reason ->
                    viewModel.acceptBid(sellerControlPost!!.id, bidId, reason)
                }
            )
        }

        if (showNotificationSheet) {
            NotificationSheet(
                notifications = notifications,
                currentUserId = currentUser.id,
                lang = language,
                onDismiss = { showNotificationSheet = false },
                onSelectNotification = { notif ->
                    showNotificationSheet = false
                    if (notif.chatId != null) {
                        viewModel.openChat(notif.chatId)
                    } else if (notif.postId != null) {
                        posts.find { it.id == notif.postId }?.let { viewModel.openPostDetail(it) }
                    }
                }
            )
        }
    }
}
