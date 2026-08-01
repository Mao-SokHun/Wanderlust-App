package com.example.wanderlust.data.repository

import com.example.wanderlust.data.SessionManager
import com.example.wanderlust.data.model.ChatApiMessage
import com.example.wanderlust.data.model.ChatApiSendRequest

class ChatRepository {

    suspend fun getChatHistory(partnerId: String): Result<List<ChatApiMessage>> {
        if (SessionManager.token.isNullOrBlank()) return Result.failure(Exception("Not logged in"))
        return apiCall { api ->
            api.getChatHistory("Bearer ${SessionManager.token}", partnerId)
        }
    }

    suspend fun sendChatMessage(partnerId: String, message: String): Result<ChatApiMessage> {
        if (SessionManager.token.isNullOrBlank()) return Result.failure(Exception("Not logged in"))
        return apiCall { api ->
            api.sendChatMessage(
                "Bearer ${SessionManager.token}",
                partnerId,
                ChatApiSendRequest(message)
            )
        }
    }
}
