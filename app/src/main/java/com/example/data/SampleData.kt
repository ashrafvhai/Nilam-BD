package com.example.data

import com.example.model.*

object SampleData {

    // 100% Authentic Bangladeshi User Profiles
    val currentUser = User(
        id = "user_me",
        name = "তানভীর আহমেদ",
        avatar = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200&auto=format&fit=crop&q=80",
        isTrusted = true,
        rating = 4.9f,
        reviewCount = 18,
        fairnessScore = 98,
        phone = "01711-987654",
        isCurrentUser = true,
        totalWonAuctions = 4,
        totalSales = 6
    )

    val userFarhana = User(
        id = "user_farhana",
        name = "ফারহানা আক্তার",
        avatar = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=200&auto=format&fit=crop&q=80",
        isTrusted = true,
        rating = 5.0f,
        reviewCount = 32,
        fairnessScore = 100,
        phone = "01912-334455"
    )

    val userMehedi = User(
        id = "user_mehedi",
        name = "মেহেদী হাসান",
        avatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&auto=format&fit=crop&q=80",
        isTrusted = true,
        rating = 4.8f,
        reviewCount = 26,
        fairnessScore = 95,
        phone = "01720-112233"
    )

    val userSadia = User(
        id = "user_sadia",
        name = "সাদিয়া ইসলাম",
        avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&auto=format&fit=crop&q=80",
        isTrusted = true,
        rating = 4.9f,
        reviewCount = 21,
        fairnessScore = 96,
        phone = "01314-556677"
    )

    val userRafiq = User(
        id = "user_rafiq",
        name = "রফিক চৌধুরী",
        avatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&auto=format&fit=crop&q=80",
        isTrusted = true,
        rating = 4.7f,
        reviewCount = 19,
        fairnessScore = 93,
        phone = "01819-223344"
    )

    val userNusrat = User(
        id = "user_nusrat",
        name = "নুসরাত জাহান",
        avatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200&auto=format&fit=crop&q=80",
        isTrusted = true,
        rating = 4.8f,
        reviewCount = 14,
        fairnessScore = 94,
        phone = "01988-776655"
    )

    val userAriful = User(
        id = "user_ariful",
        name = "আরিফুল হক",
        avatar = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200&auto=format&fit=crop&q=80",
        isTrusted = true,
        rating = 4.9f,
        reviewCount = 17,
        fairnessScore = 97,
        phone = "01611-445566"
    )

    val userMahinur = User(
        id = "user_mahinur",
        name = "মাহিনুর রহমান",
        avatar = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=200&auto=format&fit=crop&q=80",
        isTrusted = true,
        rating = 4.6f,
        reviewCount = 15,
        fairnessScore = 91,
        phone = "01552-667788"
    )

    // Realistic Bangladeshi Auction Feed
    val initialPosts = listOf(
        AuctionPost(
            id = "post_1",
            title = "iPhone 15 Pro Max 256GB Desert Titanium (অফিসিয়াল ওয়ারেন্টি)",
            description = "একদম ফ্রেশ কন্ডিশন, কোনো দাগ বা স্ক্র্যাচ নেই। ব্যাটারি হেলথ ৯৮%। অফিসিয়াল ১ বছরের অ্যাপল ওয়ারেন্টি এখনও ৫ মাস বাকি আছে। সম্পূর্ণ বক্স, অরিজিনাল ক্যাবল ও মানি রিসিট দেওয়া হবে। শুধুমাত্র আগ্রহী বায়াররা বিড করুন।",
            category = Category.MOBILE,
            condition = ItemCondition.LIKE_NEW,
            location = "ধানমন্ডি ২৭, ঢাকা",
            startingPrice = 115000L,
            currentHighestBid = 128000L,
            endTimeMillis = System.currentTimeMillis() + 450000L, // 7.5 mins remaining
            seller = userFarhana,
            images = listOf(
                "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1695048065052-870b925b64c6?w=600&auto=format&fit=crop&q=80"
            ),
            isBoosted = true,
            bids = listOf(
                Bid(
                    id = "bid_1_1",
                    postId = "post_1",
                    bidder = userMehedi,
                    amount = 120000L,
                    timestampMillis = System.currentTimeMillis() - 720000L,
                    message = "আজকেই ক্যাশ নিয়ে সরাসরি দেখা করতে পারব আপু।",
                    quickChip = "ক্যাশ অন ডেলিভারি দেওয়া যাবে?"
                ),
                Bid(
                    id = "bid_1_2",
                    postId = "post_1",
                    bidder = userMahinur,
                    amount = 125000L,
                    timestampMillis = System.currentTimeMillis() - 300000L,
                    message = "অরিজিনাল ক্যাশ মেমো আছে তো?",
                    quickChip = "পণ্যটা আসল তো?"
                ),
                Bid(
                    id = "bid_1_3",
                    postId = "post_1",
                    bidder = currentUser,
                    amount = 128000L,
                    timestampMillis = System.currentTimeMillis() - 60000L,
                    message = "ধানমন্ডি স্টার কাবাবের সামনে মিট করতে পারলে ভালো হয়।",
                    quickChip = "ডেলিভারি হবে?"
                )
            )
        ),
        AuctionPost(
            id = "post_2",
            title = "Yamaha R15 V3 Monster Edition (ঢাকা মেট্রো ল সিরিয়াল)",
            description = "একদম ফ্রেশ কন্ডিশন। মাত্র ১২,৪০০ কিমি চলা। ব্যক্তিগত যত্নে ব্যবহার করা হয়েছে, কোনো অ্যাকসিডেন্টাল রেকর্ড নেই। ডিজিটাল নাম্বার প্লেট ও স্মার্ট কার্ড রেডি। জরুরি টাকার প্রয়োজনে বিক্রি করা হবে। মিরপুরে সরাসরি এসে টেস্ট রাইড দেওয়া যাবে।",
            category = Category.CAR,
            condition = ItemCondition.LIKE_NEW,
            location = "মিরপুর ১০, ঢাকা",
            startingPrice = 320000L,
            currentHighestBid = 345000L,
            endTimeMillis = System.currentTimeMillis() + 86400000L, // 24 hours
            seller = userMehedi,
            images = listOf(
                "https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1558981403-c5f9899a28bc?w=600&auto=format&fit=crop&q=80"
            ),
            isBoosted = true,
            bids = listOf(
                Bid(
                    id = "bid_2_1",
                    postId = "post_2",
                    bidder = userAriful,
                    amount = 330000L,
                    timestampMillis = System.currentTimeMillis() - 3600000L,
                    message = "বিআরটিএ থেকে নাম ট্রান্সফার দ্রুত করে দিতে পারবেন কি?",
                    quickChip = "ডেলিভারি হবে?"
                ),
                Bid(
                    id = "bid_2_2",
                    postId = "post_2",
                    bidder = userRafiq,
                    amount = 345000L,
                    timestampMillis = System.currentTimeMillis() - 1200000L,
                    message = "বাইকের টায়ার এবং চেইনের কন্ডিশন কেমন আছে ভাই?"
                )
            )
        ),
        AuctionPost(
            id = "post_3",
            title = "ঐতিহ্যবাহী ১০০ কাউন্ট আদি ঢাকai জামদানি শাড়ি (হাতে বোনা)",
            description = "আসল নারায়ণগঞ্জের রূপগঞ্জের মাস্টার উইভারদের হাতে বোনা পিওর ১০০ কাউন্টের ঢাকাই আদি জামদানি। রঙ পাকা এবং নিখুঁত কাজ। বিয়ে বা স্পেশাল অনুষ্ঠানের জন্য অসাধারণ। একদম নতুন, আনস্টিচড ম্যাচিং ব্লাউজ পিস সহ।",
            category = Category.FASHION,
            condition = ItemCondition.NEW,
            location = "জিন্দাবাজার, সিলেট",
            startingPrice = 14500L,
            currentHighestBid = 18000L,
            endTimeMillis = System.currentTimeMillis() + 43200000L, // 12 hours
            seller = userSadia,
            images = listOf(
                "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=600&auto=format&fit=crop&q=80"
            ),
            isBoosted = false,
            bids = listOf(
                Bid(
                    id = "bid_3_1",
                    postId = "post_3",
                    bidder = userFarhana,
                    amount = 16000L,
                    timestampMillis = System.currentTimeMillis() - 7200000L,
                    message = "ঢাকার ঠিকানায় সুন্দরবন কুরিয়ারে পাঠানো যাবে আপু?",
                    quickChip = "ডেলিভারি হবে?"
                ),
                Bid(
                    id = "bid_3_2",
                    postId = "post_3",
                    bidder = userNusrat,
                    amount = 18000L,
                    timestampMillis = System.currentTimeMillis() - 1800000L,
                    message = "জরুরি প্রয়োজন ছিল, বিড একসেপ্ট করলে ক্যাশ পাঠাতে পারব।"
                )
            )
        ),
        AuctionPost(
            id = "post_4",
            title = "Sony Alpha A7 IV মিররলেস ক্যামেরা + 24-70mm GM লেন্স",
            description = "প্রফেশনাল ফটোগ্রাফি বা সিনেমাটোগ্রাফির জন্য সেরা ফুলফ্রেম ক্যামেরা। শাটার কাউন্ট মাত্র ৮,০০০। সম্পূর্ণ অরিজিনাল বক্স, চার্জার ও ২টি ব্যাটারি দেওয়া হবে। ক্যামেরাটিতে কোনো ধরনের ইন্টারনাল বা এক্সটারনাল সমস্যা নেই।",
            category = Category.ELECTRONICS,
            condition = ItemCondition.LIKE_NEW,
            location = "গুলশান ২, ঢাকা",
            startingPrice = 210000L,
            currentHighestBid = 225000L,
            endTimeMillis = System.currentTimeMillis() + 600000L, // 10 mins
            seller = currentUser, // Current user post for testing seller controls!
            images = listOf(
                "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=600&auto=format&fit=crop&q=80"
            ),
            isBoosted = true,
            bids = listOf(
                Bid(
                    id = "bid_4_1",
                    postId = "post_4",
                    bidder = userMehedi,
                    amount = 218000L,
                    timestampMillis = System.currentTimeMillis() - 2400000L,
                    message = "ভাইয়া লেন্সের গ্লাসে কোনো ফাঙ্গাস বা ডাস্ট নেই তো?",
                    quickChip = "পণ্যটা আসল তো?"
                ),
                Bid(
                    id = "bid_4_2",
                    postId = "post_4",
                    bidder = userMahinur,
                    amount = 225000L,
                    timestampMillis = System.currentTimeMillis() - 600000L,
                    message = "গুলশানে সরাসরি দেখে পেমেন্ট করব ভাইয়া।"
                )
            )
        ),
        AuctionPost(
            id = "post_5",
            title = "চিটাগাং সেগুন কাঠের কিং সাইজ খোদাই করা বক্স খাট (৬x৭ ফিট)",
            description = "শতভাগ পাকা চিটাগাং সেগুন কাঠের তৈরি। সাইজ ৬ ফিট / ৭ ফিট। অত্যন্ত মজবুত ও ভারী ফ্রেম, হাত দিয়ে নিখুঁত খোদাই করা ক্লাসিক নকশা। ফ্যামিলি শিফটিংয়ের কারণে বিক্রি করা হচ্ছে।",
            category = Category.FURNITURE,
            condition = ItemCondition.USED,
            location = "জিইসি মোড়, চট্টগ্রাম",
            startingPrice = 38000L,
            currentHighestBid = 42000L,
            endTimeMillis = System.currentTimeMillis() + 72000000L,
            seller = userRafiq,
            images = listOf(
                "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=600&auto=format&fit=crop&q=80"
            ),
            isBoosted = false,
            bids = listOf(
                Bid(
                    id = "bid_5_1",
                    postId = "post_5",
                    bidder = userAriful,
                    amount = 42000L,
                    timestampMillis = System.currentTimeMillis() - 900000L,
                    message = "কাঠের বার্নিশ কি এখনও নতুনের মতোই আছে?",
                    quickChip = "পণ্যটা আসল তো?"
                )
            )
        ),
        AuctionPost(
            id = "post_6",
            title = "MacBook Air M2 16GB RAM / 512GB SSD Space Gray",
            description = "অফিসিয়াল অ্যাপল স্টোর থেকে কেনা। ব্যাটারি হেলথ ৯৯%। সাইকেল কাউন্ট মাত্র ৩২। কোনো ডেন্ট বা স্ক্র্যাচ নেই। অরিজিনাল ৩৫ ওয়াট ফাস্ট চার্জার ও বক্স সাথে দেওয়া হবে। কোডিং এবং ভিডিও এডিটিংয়ের জন্য আদর্শ ল্যাপটপ।",
            category = Category.ELECTRONICS,
            condition = ItemCondition.LIKE_NEW,
            location = "বনানী, ঢাকা",
            startingPrice = 95000L,
            currentHighestBid = 106000L,
            endTimeMillis = System.currentTimeMillis() + 1800000L, // 30 mins
            seller = userNusrat,
            images = listOf(
                "https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=600&auto=format&fit=crop&q=80"
            ),
            isBoosted = false,
            bids = listOf(
                Bid(
                    id = "bid_6_1",
                    postId = "post_6",
                    bidder = userMehedi,
                    amount = 100000L,
                    timestampMillis = System.currentTimeMillis() - 1500000L,
                    message = "কিবোর্ড লেআউট কি ইউএস ইংলিশ?"
                ),
                Bid(
                    id = "bid_6_2",
                    postId = "post_6",
                    bidder = currentUser,
                    amount = 106000L,
                    timestampMillis = System.currentTimeMillis() - 400000L,
                    message = "বনানী ১১ নাম্বার রোডে এসে পিকআপ করতে পারব আপু।"
                )
            )
        ),
        AuctionPost(
            id = "post_7",
            title = "সুন্দরবনের চাকভাঙা অপরিশোধিত মধু ও খাঁটি গাওয়া ঘি (২ কেজি কম্বো)",
            description = "সুন্দরবনের গভীর জঙ্গল থেকে মোয়ালদের সরাসরি সংগ্রহ করা খাঁটি চাকভাঙা খলিসা ফুলের মধু (১ কেজি) এবং পাবনার দেশি গরুর দুধের গাওয়া ঘি (১ কেজি)। শতভাগ খাঁটি ও ফ্রেশ গ্যারান্টি।",
            category = Category.OTHERS,
            condition = ItemCondition.NEW,
            location = "বোয়ালিয়া, রাজশাহী",
            startingPrice = 2200L,
            currentHighestBid = 2800L,
            endTimeMillis = System.currentTimeMillis() + 36000000L,
            seller = userAriful,
            images = listOf(
                "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=600&auto=format&fit=crop&q=80"
            ),
            isBoosted = false,
            bids = listOf(
                Bid(
                    id = "bid_7_1",
                    postId = "post_7",
                    bidder = userFarhana,
                    amount = 2500L,
                    timestampMillis = System.currentTimeMillis() - 5000000L,
                    message = "মধু কি একদম আনফিল্টার্ড এবং কাঁচা মধু?"
                ),
                Bid(
                    id = "bid_7_2",
                    postId = "post_7",
                    bidder = userSadia,
                    amount = 2800L,
                    timestampMillis = System.currentTimeMillis() - 1800000L,
                    message = "সিলেটে হোম ডেলিভারি দিতে পারবেন ভাইয়া?"
                )
            )
        )
    )

    val initialChats = listOf(
        ChatConversation(
            id = "chat_1",
            postId = "post_1",
            postTitle = "iPhone 15 Pro Max 256GB Desert Titanium",
            postPrice = 128000L,
            postImage = "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=300&auto=format&fit=crop&q=80",
            seller = userFarhana,
            buyer = currentUser,
            lastMessage = "ধানমন্ডি স্টার কাবাবের সামনে মিট করতে পারলে ভালো হয়।",
            lastMessageTime = System.currentTimeMillis() - 60000L,
            unreadCount = 0
        )
    )

    val initialMessages = mapOf(
        "chat_1" to listOf(
            ChatMessage(
                id = "m_1",
                chatId = "chat_1",
                senderId = "system",
                senderName = "নিলাম",
                text = "🎉 ফারহানা আক্তার আপনার ৳ ১২৮,০০০ বিডটি গ্রহণ করেছেন। চ্যাটে কথা বলে ডেলিভারি ও লেনদেনের স্থান নির্ধারণ করুন।",
                timestampMillis = System.currentTimeMillis() - 180000L,
                isMine = false,
                isSystem = true
            ),
            ChatMessage(
                id = "m_2",
                chatId = "chat_1",
                senderId = "user_farhana",
                senderName = "ফারহানা আক্তার",
                text = "আসসালামু আলাইকুম তানভীর ভাই! আপনার বিডটি একসেপ্ট করেছি। কখন দেখা করতে পারবেন?",
                timestampMillis = System.currentTimeMillis() - 120000L,
                isMine = false
            ),
            ChatMessage(
                id = "m_3",
                chatId = "chat_1",
                senderId = "user_me",
                senderName = "তানভীর আহমেদ",
                text = "ওয়ালাইকুম আসসালাম! আজ সন্ধ্যায় কি ধানমন্ডি স্টার কাবাবের সামনে আসা সম্ভব?",
                timestampMillis = System.currentTimeMillis() - 60000L,
                isMine = true
            )
        )
    )

    // Strictly targeted notifications complying with user's 3-notification rule:
    // 1) Post owner notified when a bid is placed
    // 2) Winning bidder notified when their bid is accepted
    // 3) Outbid bidder notified when someone bids higher
    val initialNotifications = listOf(
        AppNotification(
            id = "notif_init_1",
            titleBn = "🎉 আপনার বিডটি একসেপ্ট হয়েছে!",
            titleEn = "🎉 Your bid was accepted!",
            bodyBn = "ফারহানা আক্তার 'iPhone 15 Pro Max 256GB' পণ্যে আপনার ৳ ১২৮,০০০ বিডটি একসেপ্ট করেছেন। ইনবক্সে চ্যাট করুন।",
            bodyEn = "Farhana Akter accepted your bid of ৳ 128,000 for 'iPhone 15 Pro Max 256GB'. Chat now.",
            timestampMillis = System.currentTimeMillis() - 180000L,
            read = false,
            postId = "post_1",
            chatId = "chat_1",
            recipientUserId = "user_me",
            type = NotificationType.WINNER
        ),
        AppNotification(
            id = "notif_init_2",
            titleBn = "আপনার পোস্টে নতুন বিড এসেছে!",
            titleEn = "New bid on your post!",
            bodyBn = "মাহিনুর রহমান আপনার 'Sony Alpha A7 IV মিররলেস ক্যামেরা' পণ্যে ৳ ২২৫,০০০ বিড করেছেন।",
            bodyEn = "Mahinur Rahman bid ৳ 225,000 on your 'Sony Alpha A7 IV Mirrorless Camera'.",
            timestampMillis = System.currentTimeMillis() - 600000L,
            read = true,
            postId = "post_4",
            recipientUserId = "user_me",
            type = NotificationType.BID
        ),
        AppNotification(
            id = "notif_init_3",
            titleBn = "⚠️ কেউ আপনার চেয়ে বেশি বিড করেছে!",
            titleEn = "⚠️ You have been outbid!",
            bodyBn = "মেহেদী হাসান 'MacBook Air M2' পণ্যে আপনার চেয়ে বেশি ৳ ১০৬,০০০ বিড করেছেন।",
            bodyEn = "Mehedi Hasan outbid you with ৳ 106,000 on 'MacBook Air M2'.",
            timestampMillis = System.currentTimeMillis() - 1400000L,
            read = true,
            postId = "post_6",
            recipientUserId = "user_me",
            type = NotificationType.OUTBID
        )
    )
}
