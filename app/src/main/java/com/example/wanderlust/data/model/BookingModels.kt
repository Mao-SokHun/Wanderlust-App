package com.example.wanderlust.data.model

/** Guest booking inquiry (request-to-book) for a business listing. */
data class BookingRequest(
    val id: String = "",
    val tourId: String = "",
    val tourTitle: String = "",
    val guestId: String = "",
    val guestName: String = "",
    val guestEmail: String = "",
    val guestPhone: String = "",
    val businessId: String? = null,
    val businessName: String = "",
    val travelDate: String? = null,
    val guests: Int = 1,
    val message: String = "",
    val status: String = "pending",
    val businessReply: String = "",
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val listingType: String = "TOUR",
    val priceLabel: String = "",
    val imageUrl: String = "",
)

data class CreateBookingRequestBody(
    val tourId: String,
    val travelDate: String? = null,
    val guests: Int = 1,
    val message: String = "",
    val guestPhone: String = "",
)

data class CreateBookingRequestResponse(
    val request: BookingRequest,
    val message: String = "",
    val messageKh: String = "",
)

data class BookingRequestsResponse(
    val requests: List<BookingRequest> = emptyList(),
)

data class UpdateInboxRequestBody(
    val status: String,
    val businessReply: String = "",
)

data class UpdateInboxResponse(
    val request: BookingRequest,
)
