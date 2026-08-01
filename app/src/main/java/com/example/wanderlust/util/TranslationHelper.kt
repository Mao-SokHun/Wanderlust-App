package com.example.wanderlust.util

import com.example.wanderlust.data.model.TranslateRequest
import com.example.wanderlust.data.repository.apiCall

object TranslationHelper {

    suspend fun translate(text: String, targetLang: String = "en"): String {
        if (text.isBlank()) return text
        return try {
            val res = apiCall { api ->
                api.translateText(TranslateRequest(text, targetLang))
            }
            res.getOrNull()?.translatedText ?: text
        } catch (_: Exception) {
            text
        }
    }
}
