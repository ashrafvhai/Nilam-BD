package com.example.model

enum class Language {
    BN, EN
}

enum class Category(val labelBn: String, val labelEn: String, val icon: String) {
    ALL("সব", "All", "🏷️"),
    MOBILE("মোবাইল", "Mobile", "📱"),
    CAR("গাড়ি", "Car", "🚗"),
    FASHION("ফ্যাশন", "Fashion", "👕"),
    ELECTRONICS("ইলেকট্রনিক্স", "Electronics", "💻"),
    FURNITURE("ফার্নিচার", "Furniture", "🛋️"),
    OTHERS("অন্যান্য", "Others", "📦")
}

enum class ItemCondition(val labelBn: String, val labelEn: String) {
    NEW("একদম নতুন (Brand New)", "Brand New"),
    LIKE_NEW("নতুনের মত (Like New)", "Like New"),
    USED("ব্যবহৃত (Used)", "Used")
}

enum class SortOption(val labelBn: String, val labelEn: String) {
    ENDING_SOON("দ্রুত শেষ হচ্ছে", "Ending Soon"),
    NEWEST("নতুন পোস্ট", "Newest"),
    HIGHEST_BID("সর্বোচ্চ বিড", "Highest Bid")
}

data class User(
    val id: String,
    val name: String,
    val avatar: String,
    val isTrusted: Boolean = false,
    val rating: Float = 5.0f,
    val reviewCount: Int = 12,
    val fairnessScore: Int = 95, // % of times seller chose the highest bidder
    val strikes: Int = 0, // 0 = Clean, 1 = Warning, 2 = 7-Day Ban, 3 = Permanent Ban
    val bannedUntilMillis: Long? = null,
    val phone: String = "01712-345678",
    val isCurrentUser: Boolean = false,
    val totalWonAuctions: Int = 3,
    val totalSales: Int = 8
)

data class Bid(
    val id: String,
    val postId: String,
    val bidder: User,
    val amount: Long,
    val timestampMillis: Long,
    val message: String = "",
    val quickChip: String? = null,
    val isAccepted: Boolean = false
)

data class AuctionPost(
    val id: String,
    val title: String,
    val description: String,
    val category: Category,
    val condition: ItemCondition,
    val location: String,
    val startingPrice: Long,
    val currentHighestBid: Long,
    val bids: List<Bid> = emptyList(),
    val endTimeMillis: Long,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val seller: User,
    val images: List<String> = emptyList(),
    val isClosed: Boolean = false,
    val winningBidId: String? = null,
    val winnerChoiceReason: String? = null,
    val isBoosted: Boolean = false,
    val antiSnipedCount: Int = 0,
    val dealCompletedBuyer: Boolean = false,
    val dealCompletedSeller: Boolean = false,
    val buyerReported: Boolean = false,
    val reportReason: String? = null
)

data class ChatMessage(
    val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val timestampMillis: Long,
    val isMine: Boolean,
    val isSystem: Boolean = false
)

data class ChatConversation(
    val id: String,
    val postId: String,
    val postTitle: String,
    val postPrice: Long,
    val postImage: String,
    val seller: User,
    val buyer: User,
    val lastMessage: String,
    val lastMessageTime: Long,
    val unreadCount: Int = 0,
    val dealCompletedBuyer: Boolean = false,
    val dealCompletedSeller: Boolean = false,
    val reportStatus: String? = null // e.g. "DID_NOT_BUY", "RESOLVED"
)

data class AppNotification(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val bodyBn: String,
    val bodyEn: String,
    val timestampMillis: Long,
    val read: Boolean = false,
    val postId: String? = null,
    val chatId: String? = null,
    val recipientUserId: String? = null,
    val type: NotificationType = NotificationType.INFO
)


enum class NotificationType {
    BID, WINNER, OUTBID, WARNING, SUCCESS, INFO
}

object BanglaUtils {
    private val bnDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

    fun toBanglaDigits(numberStr: String): String {
        val sb = StringBuilder()
        for (ch in numberStr) {
            if (ch in '0'..'9') {
                sb.append(bnDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }

    fun formatPrice(amount: Long, lang: Language): String {
        val formatted = "%,d".format(amount)
        return if (lang == Language.BN) {
            "৳ " + toBanglaDigits(formatted)
        } else {
            "৳ " + formatted
        }
    }

    fun formatDuration(remainingMillis: Long, lang: Language): String {
        if (remainingMillis <= 0) {
            return if (lang == Language.BN) "নিলাম শেষ" else "Ended"
        }
        val totalSec = remainingMillis / 1000
        val days = totalSec / (24 * 3600)
        val hours = (totalSec % (24 * 3600)) / 3600
        val mins = (totalSec % 3600) / 60
        val secs = totalSec % 60

        return if (lang == Language.BN) {
            when {
                days > 0 -> "${toBanglaDigits(days.toString())} দিন ${toBanglaDigits(hours.toString())} ঘণ্টা"
                hours > 0 -> "${toBanglaDigits(hours.toString())} ঘণ্টা ${toBanglaDigits(mins.toString())} মি."
                mins > 0 -> "${toBanglaDigits(mins.toString())} মিনিট ${toBanglaDigits(secs.toString())} সে."
                else -> "${toBanglaDigits(secs.toString())} সেকেন্ড"
            }
        } else {
            when {
                days > 0 -> "${days}d ${hours}h"
                hours > 0 -> "${hours}h ${mins}m"
                mins > 0 -> "${mins}m ${secs}s"
                else -> "${secs}s left"
            }
        }
    }

    fun formatRelativeTime(timestampMillis: Long, lang: Language): String {
        val diffMillis = System.currentTimeMillis() - timestampMillis
        val diffSec = diffMillis / 1000
        val diffMin = diffSec / 60
        val diffHours = diffMin / 60

        return if (lang == Language.BN) {
            when {
                diffMin < 1 -> "এইমাত্র"
                diffMin < 60 -> "${toBanglaDigits(diffMin.toString())} মিনিট আগে"
                diffHours < 24 -> "${toBanglaDigits(diffHours.toString())} ঘণ্টা আগে"
                else -> "${toBanglaDigits((diffHours / 24).toString())} দিন আগে"
            }
        } else {
            when {
                diffMin < 1 -> "Just now"
                diffMin < 60 -> "${diffMin}m ago"
                diffHours < 24 -> "${diffHours}h ago"
                else -> "${diffHours / 24}d ago"
            }
        }
    }
}
