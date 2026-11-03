package com.example

import com.example.model.BanglaUtils
import com.example.model.Language
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun banglaDigitsConversion_isCorrect() {
        val converted = BanglaUtils.toBanglaDigits("0123456789")
        assertEquals("০১২৩৪৫৬৭৮৯", converted)
    }

    @Test
    fun banglaPriceFormatting_isCorrect() {
        val priceBn = BanglaUtils.formatPrice(128000L, Language.BN)
        assertEquals("৳ ১২৮,০০০", priceBn)

        val priceEn = BanglaUtils.formatPrice(128000L, Language.EN)
        assertEquals("৳ 128,000", priceEn)
    }

    @Test
    fun bangladeshiProfiles_arePresent() {
        val currentUser = com.example.data.SampleData.currentUser
        assertEquals("তানভীর আহমেদ", currentUser.name)
        assertTrue(currentUser.phone.startsWith("017"))

        val posts = com.example.data.SampleData.initialPosts
        assertTrue(posts.isNotEmpty())
        assertTrue(posts.any { it.seller.name.contains("ফারহানা") })
        assertTrue(posts.any { it.seller.name.contains("মেহেদী") })
        assertTrue(posts.any { it.location.contains("ঢাকা") })
    }
}
