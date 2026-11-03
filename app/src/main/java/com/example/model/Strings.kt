package com.example.model

object NilamStrings {
    // Navigation
    fun home(lang: Language) = if (lang == Language.BN) "হোম" else "Home"
    fun search(lang: Language) = if (lang == Language.BN) "সার্চ" else "Search"
    fun post(lang: Language) = if (lang == Language.BN) "পোস্ট" else "Post"
    fun inbox(lang: Language) = if (lang == Language.BN) "ইনবক্স" else "Inbox"
    fun profile(lang: Language) = if (lang == Language.BN) "প্রোফাইল" else "Profile"

    // App header & slogans
    fun appTitle(lang: Language) = if (lang == Language.BN) "নিলাম" else "Nilam"
    fun appTagline(lang: Language) = if (lang == Language.BN) "বাংলাদেশের বিশ্বস্ত নিলাম প্ল্যাটফর্ম" else "Bangladesh's Trusted Auction"
    fun sellerDecisionFinal(lang: Language) = if (lang == Language.BN) "বিক্রেতার সিদ্ধান্তই চূড়ান্ত" else "Seller's decision is final"
    fun trustedBadge(lang: Language) = if (lang == Language.BN) "✔ ট্রাস্টেড" else "✔ Trusted"
    fun getVerified(lang: Language) = if (lang == Language.BN) "NID ভেরিফাই করুন" else "Verify NID"
    fun privacyNote(lang: Language) = if (lang == Language.BN)
        "আপনার NID তথ্য এনক্রিপ্ট করে রাখা হয় এবং কাউকে দেখানো হয় না।"
    else
        "Your NID info is securely encrypted and never shared with anyone."

    // Feed & Post Card
    fun trustedOnly(lang: Language) = if (lang == Language.BN) "শুধু বিশ্বস্ত বিক্রেতা" else "Trusted sellers only"
    fun currentHighestBid(lang: Language) = if (lang == Language.BN) "বর্তমান সর্বোচ্চ বিড" else "Current Highest Bid"
    fun startPrice(lang: Language) = if (lang == Language.BN) "শুরুর মূল্য" else "Starting Price"
    fun totalBids(count: Int, lang: Language) = if (lang == Language.BN)
        "${BanglaUtils.toBanglaDigits(count.toString())} টি বিড"
    else
        "$count bids"
    fun placeBidBtn(lang: Language) = if (lang == Language.BN) "বিড করুন" else "Place Bid"
    fun bidHistoryBtn(lang: Language) = if (lang == Language.BN) "বিড হিস্ট্রি" else "Bid History"
    fun antiSnipingActive(lang: Language) = if (lang == Language.BN)
        "অ্যান্টি-স্নাইপিং সক্রিয়! সময় আরও ২ মিনিট বাড়ানো হয়েছে"
    else
        "Anti-sniping active! +2 mins added"

    // Bidding
    fun bidCommitmentTitle(lang: Language) = if (lang == Language.BN) "বিড মানে কেনার প্রতিশ্রুতি" else "A bid is a promise to buy"
    fun bidCommitmentDesc(lang: Language) = if (lang == Language.BN)
        "আপনি বিড করলে এবং বিক্রেতা আপনাকে বিজয়ী হিসেবে নির্বাচন করলে, পণ্যটি কেনা বাধ্যতামূলক। ব্যর্থ হলে অ্যাকাউন্টে স্ট্রাইক দেওয়া হবে।"
    else
        "By placing a bid, you commit to purchase if chosen by seller. Non-completion results in account strikes."
    fun iAgree(lang: Language) = if (lang == Language.BN) "আমি শর্ত মেনে নিচ্ছি" else "I agree to the commitment"
    fun enterBidAmount(lang: Language) = if (lang == Language.BN) "বিড পরিমাণ লিখুন" else "Enter Bid Amount"
    fun optionalMessage(lang: Language) = if (lang == Language.BN) "বার্তা লিখুন (ঐচ্ছিক, সর্বোচ্চ ১০০ অক্ষর)" else "Message (optional, max 100 chars)"
    fun confirmBid(lang: Language) = if (lang == Language.BN) "বিড নিশ্চিত করুন" else "Confirm Bid"

    // Seller Winner Control
    fun sellerControlsTitle(lang: Language) = if (lang == Language.BN) "বিক্রেতার নিয়ন্ত্রণ প্যানেল" else "Seller Control Panel"
    fun acceptThisBid(lang: Language) = if (lang == Language.BN) "বিড একসেপ্ট করুন" else "Accept Bid"
    fun reasonIfNotHighest(lang: Language) = if (lang == Language.BN) "সর্বোচ্চ বিড না নেওয়ার কারণ (ঐচ্ছিক)" else "Reason if not highest bid (optional)"
    fun reasonContactFailed(lang: Language) = if (lang == Language.BN) "যোগাযোগ করা যায়নি" else "Couldn't contact bidder"
    fun reasonNearbyLocation(lang: Language) = if (lang == Language.BN) "কাছের লোকেশন" else "Closer pickup location"
    fun reasonBetterRating(lang: Language) = if (lang == Language.BN) "উন্নত বায়ার রেটিং" else "Higher buyer rating"
    fun chooseNoOne(lang: Language) = if (lang == Language.BN) "কাউকেই নয় (বাতিল)" else "Choose No One (Close)"

    // Inbox & Chat
    fun dealCompleted(lang: Language) = if (lang == Language.BN) "বিক্রি সম্পন্ন" else "Deal Completed"
    fun dealCompletedBoth(lang: Language) = if (lang == Language.BN) "উভয়পক্ষ বিক্রি সম্পন্ন নিশ্চিত করেছে!" else "Both parties confirmed deal!"
    fun waitingPartnerConfirm(lang: Language) = if (lang == Language.BN) "অপরপক্ষের অনুমোদনের অপেক্ষা..." else "Waiting for other party to confirm..."
    fun reportDidNotBuy(lang: Language) = if (lang == Language.BN) "কেনেননি বলে রিপোর্ট করুন" else "Report 'Did not buy'"
    fun reportSuccess(lang: Language) = if (lang == Language.BN) "রিপোর্ট দাখিল হয়েছে" else "Report submitted"
    fun typeMessage(lang: Language) = if (lang == Language.BN) "মেসেজ লিখুন..." else "Type a message..."

    // Verification
    fun nidStep1Title(lang: Language) = if (lang == Language.BN) "১. জাতীয় পরিচয়পত্রের ছবি (Front & Back)" else "1. NID Photos (Front & Back)"
    fun nidStep2Title(lang: Language) = if (lang == Language.BN) "২. লাইভ সেলফি ভেরিফিকেশন" else "2. Live Selfie Verification"
    fun nidStep3Title(lang: Language) = if (lang == Language.BN) "৩. মোবাইল নম্বর ও ওটিপি (OTP)" else "3. Mobile & OTP"
    fun verifyingAI(lang: Language) = if (lang == Language.BN) "AI তথ্য যাচাই করছে..." else "AI verifying documents..."
    fun verificationSuccess(lang: Language) = if (lang == Language.BN) "অভিনন্দন! আপনি এখন ট্রাস্টেড সদস্য" else "Congratulations! You are now Trusted"

    // Profile & Fairness
    fun fairnessScore(score: Int, lang: Language) = if (lang == Language.BN)
        "ফেয়ারনেস স্কোর: ${BanglaUtils.toBanglaDigits(score.toString())}%"
    else
        "Fairness Score: $score%"
    fun fairnessExpl(lang: Language) = if (lang == Language.BN)
        "বিক্রেতা কতবার সর্বোচ্চ বিডারকে বিজয়ী বানিয়েছেন"
    else
        "How often the seller picked the highest bidder"
    fun strikesStatus(strikes: Int, lang: Language) = if (lang == Language.BN) {
        when (strikes) {
            0 -> "স্বচ্ছ অ্যাকাউন্ট (কোনো স্ট্রাইক নেই)"
            1 -> "সতর্কবার্তা (১ম স্ট্রাইক)"
            2 -> "৭ দিনের জন্য বিড নিষিদ্ধ (২য় স্ট্রাইক)"
            else -> "স্থায়ীভাবে নিষিদ্ধ (৩য় স্ট্রাইক)"
        }
    } else {
        when (strikes) {
            0 -> "Clean account (No strikes)"
            1 -> "Warning issued (1st strike)"
            2 -> "7-day Bidding Ban (2nd strike)"
            else -> "Permanently Banned (3rd strike)"
        }
    }

    // AI Suggest
    fun aiSuggestBtn(lang: Language) = if (lang == Language.BN) "✨ AI বিবরণ ও দাম পরামর্শ" else "✨ AI Suggest Description & Price"
    fun aiGenerating(lang: Language) = if (lang == Language.BN) "AI চিন্তা করছে..." else "AI Thinking..."

    // Featured boost
    fun boostPost(lang: Language) = if (lang == Language.BN) "⚡ পোস্ট বুস্ট করুন (ফিচার্ড)" else "⚡ Boost Post (Featured)"
    fun payWithBkash(lang: Language) = if (lang == Language.BN) "বিকাশ পেমেন্ট (৳ ১০০)" else "bKash Pay (৳ 100)"
    fun payWithNagad(lang: Language) = if (lang == Language.BN) "নগদ পেমেন্ট (৳ ১০০)" else "Nagad Pay (৳ 100)"
}
