package com.example.wanderlust.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Currency utility for dual-currency support ($ USD and ៛ KHR).
 * Converts prices based on live/configured exchange rate (default: $1 = 4,100 KHR).
 */
object CurrencyUtils {

    const val DEFAULT_USD_TO_KHR_RATE = 4100.0

    enum class CurrencyMode {
        USD,
        KHR,
    }

    /** Active currency preference (defaults to USD). */
    var activeCurrency: CurrencyMode = CurrencyMode.USD

    /**
     * Formats price in USD or KHR based on active currency preference.
     * Example: formatPrice(25.0) -> "$25.00" or "102,500 ៛"
     */
    fun formatPrice(
        priceUsd: Double?,
        rate: Double = DEFAULT_USD_TO_KHR_RATE,
        mode: CurrencyMode = activeCurrency,
    ): String {
        if (priceUsd == null || priceUsd <= 0.0) return "Free / ឥតគិតថ្លៃ"

        return when (mode) {
            CurrencyMode.USD -> {
                val fmt = NumberFormat.getCurrencyInstance(Locale.US)
                fmt.format(priceUsd)
            }
            CurrencyMode.KHR -> {
                val khrValue = (priceUsd * rate).toLong()
                val fmt = NumberFormat.getNumberInstance(Locale.US)
                "${fmt.format(khrValue)} ៛"
            }
        }
    }

    /** Formats both USD and KHR for dual-display (e.g. "$25 / 102,500 ៛"). */
    fun formatDualPrice(
        priceUsd: Double?,
        rate: Double = DEFAULT_USD_TO_KHR_RATE,
    ): String {
        if (priceUsd == null || priceUsd <= 0.0) return "Free / ឥតគិតថ្លៃ"
        val usdFmt = NumberFormat.getCurrencyInstance(Locale.US)
        val khrValue = (priceUsd * rate).toLong()
        val khrFmt = NumberFormat.getNumberInstance(Locale.US)
        return "${usdFmt.format(priceUsd)} · ${khrFmt.format(khrValue)} ៛"
    }
}
