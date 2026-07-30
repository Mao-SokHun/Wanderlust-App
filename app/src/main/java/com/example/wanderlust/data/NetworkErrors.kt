package com.example.wanderlust.data

import com.example.wanderlust.data.model.MessageResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private val errorGson = Gson()

fun friendlyNetworkMessage(throwable: Throwable): String {
    if (throwable is HttpException) {
        parseApiErrorMessage(throwable)?.let { apiMsg ->
            return when {
                apiMsg.contains("Invalid token", ignoreCase = true) ||
                    apiMsg.contains("Missing token", ignoreCase = true) ||
                    apiMsg.contains("jwt", ignoreCase = true) ->
                    "Your session has expired. Please sign out and sign in again."
                else -> apiMsg
            }
        }
        return when (throwable.code()) {
            401 -> "Invalid email or password. Please try again."
            403 -> "You don't have permission to do this."
            400 -> "Something looks wrong with your request. Please check your details and try again."
            404 -> "The content you're looking for could not be found."
            409 -> "An account with this email may already exist. Try signing in instead."
            500, 503 -> "Something went wrong on our end. Please try again in a moment."
            else -> "Something went wrong. Please try again."
        }
    }
    return when (throwable) {
        is ConnectException,
        is UnknownHostException ->
            "Unable to connect. Please check your internet connection and try again."
        is SocketTimeoutException ->
            "The request is taking too long. Please check your connection and retry."
        else -> {
            val raw = throwable.message.orEmpty()
            when {
                raw.contains("Failed to connect", ignoreCase = true) ->
                    "Unable to connect. Please check your internet connection and try again."
                raw.contains("CLEARTEXT", ignoreCase = true) ->
                    "Connection failed. Please update the app and try again."
                raw.startsWith("HTTP ") ->
                    parseHttpStatusLine(raw) ?: "Something went wrong. Please try again."
                else -> "Something went wrong. Please try again."
            }
        }
    }
}

private fun parseApiErrorMessage(http: HttpException): String? {
    val body = http.response()?.errorBody()?.string().orEmpty()
    if (body.isBlank()) return null
    return try {
        errorGson.fromJson(body, MessageResponse::class.java).message?.takeIf { it.isNotBlank() }
    } catch (_: Exception) {
        null
    }
}

private fun parseHttpStatusLine(raw: String): String? {
    val code = Regex("HTTP (\\d+)").find(raw)?.groupValues?.getOrNull(1)?.toIntOrNull() ?: return null
    return when (code) {
        401 -> "Invalid email or password. Please try again."
        403 -> "You don't have permission to do this."
        404 -> "The content you're looking for could not be found."
        500, 503 -> "Something went wrong on our end. Please try again in a moment."
        else -> "Something went wrong. Please try again."
    }
}
