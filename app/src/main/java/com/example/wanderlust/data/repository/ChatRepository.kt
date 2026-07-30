package com.example.wanderlust.data.repository

import com.example.wanderlust.data.SessionManager
import com.example.wanderlust.data.model.ChatApiMessage
import com.example.wanderlust.data.model.ChatApiSendRequest
import com.example.wanderlust.data.remote.ApiClient

class ChatRepository {
    private val api = ApiClient.api

    suspend fun getChatHistory(partnerId: String): Result<List<ChatApiMessage>> {
        return safeApiCall {
            val token = "Bearer ${SessionManager.token}"
            api.getChatHistory(token, partnerId)
        }
    }

    suspend fun sendChatMessage(partnerId: String, message: String): Result<ChatApiMessage> {
        return safeApiCall {
            val token = "Bearer ${SessionManager.token}"
            api.sendChatMessage(token, partnerId, ChatApiSendRequest(message))
        }
    }
}
