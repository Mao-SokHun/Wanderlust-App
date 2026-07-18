package com.example.wanderlust.data.repository

import com.example.wanderlust.data.SessionManager
import com.example.wanderlust.data.model.BookingRequest
import com.example.wanderlust.data.model.CreateBookingRequestBody
import com.example.wanderlust.data.model.CreateBookingRequestResponse
import com.example.wanderlust.data.model.UpdateInboxRequestBody

/** Guest requests + business inbox for listing inquiries. */
class BookingRepository {

    /** Guest: send a request-to-book for a published listing. */
    suspend fun createRequest(
        tourId: String,
        travelDate: String?,
        guests: Int,
        message: String,
        guestPhone: String,
    ): Result<CreateBookingRequestResponse> {
        val header = SessionManager.authHeader()
            ?: return Result.failure(Exception("Please sign in first"))
        return apiCall {
            it.createBookingRequest(
                header,
                CreateBookingRequestBody(
                    tourId = tourId,
                    travelDate = travelDate?.takeIf { d -> d.isNotBlank() },
                    guests = guests.coerceIn(1, 50),
                    message = message,
                    guestPhone = guestPhone,
                ),
            )
        }
    }

    /** Guest: my booking requests. */
    suspend fun myRequests(): Result<List<BookingRequest>> {
        val header = SessionManager.authHeader()
            ?: return Result.failure(Exception("Please sign in first"))
        return apiCall { it.getMyBookingRequests(header) }
            .map { it.requests }
    }

    /** Business: inbox of guest requests. */
    suspend fun inbox(): Result<List<BookingRequest>> {
        val header = SessionManager.authHeader()
            ?: return Result.failure(Exception("Please sign in first"))
        return apiCall { it.getBusinessInbox(header) }
            .map { it.requests }
    }

    /** Business: accept / decline / contacted + optional reply. */
    suspend fun respond(
        requestId: String,
        status: String,
        businessReply: String = "",
    ): Result<BookingRequest> {
        val header = SessionManager.authHeader()
            ?: return Result.failure(Exception("Please sign in first"))
        return apiCall {
            it.updateBusinessInbox(
                header,
                requestId,
                UpdateInboxRequestBody(status = status, businessReply = businessReply),
            )
        }.map { it.request }
    }
}
