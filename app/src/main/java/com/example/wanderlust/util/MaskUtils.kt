package com.example.wanderlust.util

/**
 * Data masking utilities — hide sensitive user data on screen.
 * DO NOT expose raw phone numbers, card numbers, or IDs to the UI.
 */
object MaskUtils {

    /**
     * Masks a phone number, revealing only the first 3 and last 2 digits.
     * e.g. "0921234589" → "092****89"
     * Short/empty numbers are returned as-is.
     */
    fun maskPhone(phone: String): String {
        val cleaned = phone.trim()
        if (cleaned.length < 6) return cleaned
        val prefix = cleaned.take(3)
        val suffix = cleaned.takeLast(2)
        return "$prefix****$suffix"
    }

    /**
     * Masks a credit/debit card number, showing only last 4 digits.
     * e.g. "4111111111111111" → "**** **** **** 1111"
     */
    fun maskCard(cardNumber: String): String {
        val digits = cardNumber.filter { it.isDigit() }
        if (digits.length < 4) return "**** **** **** ****"
        val last4 = digits.takeLast(4)
        return "**** **** **** $last4"
    }

    /**
     * Masks an email showing only first 2 chars and the domain.
     * e.g. "john.doe@gmail.com" → "jo***@gmail.com"
     */
    fun maskEmail(email: String): String {
        val atIndex = email.indexOf('@')
        if (atIndex < 3) return email
        val visible = email.take(2)
        val domain = email.substring(atIndex)
        return "$visible***$domain"
    }
}
