package com.example.wanderlust.data.repository

import com.example.wanderlust.data.SessionManager
import com.example.wanderlust.data.model.BookTourRequest
import com.example.wanderlust.data.model.BookTourResponse
import com.example.wanderlust.data.model.UserBooking

class BookingRepository {

    suspend fun getMyBookings(): Result<List<UserBooking>> {
        val token = SessionManager.token
        if (token.isNullOrBlank()) return Result.failure(Exception("Not logged in"))
        return apiCall { api -> api.getMyBookings("Bearer $token") }
    }

    suspend fun bookTour(
        tourId: String,
        passengerName: String = "",
        travelDate: String = "",
        seatNumber: String = "",
        specialRequests: String = "",
    ): Result<BookTourResponse> {
        val token = SessionManager.token
        if (token.isNullOrBlank()) return Result.failure(Exception("Not logged in"))
        return apiCall { api ->
            api.bookTour(
                token = "Bearer $token",
                id = tourId,
                request = BookTourRequest(
                    passengerName = passengerName,
                    seatNumber = seatNumber,
                    travelDate = travelDate,
                    specialRequests = specialRequests,
                ),
            )
        }
    }
}
