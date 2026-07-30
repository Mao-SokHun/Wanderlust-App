package com.example.wanderlust.data.model

/** Status of a traveler's ticket / reservation. */
enum class BookingStatus {
    ACTIVE,
    COMPLETED,
    CANCELED,
}

/** Digital ticket representation for a bus trip, tour package, or vehicle rental. */
data class BookingTicket(
    val ticketId: String,
    val bookingRef: String,
    val listingId: String,
    val title: String,
    val listingType: String = "TRIP", // TRIP, TOUR, RENTAL
    val departureCity: String = "",
    val arrivalCity: String = "",
    val travelDate: String = "",
    val departureTime: String = "",
    val seatNumber: String = "",
    val vehicleType: String = "",
    val passengerName: String = "",
    val priceUsd: Double = 0.0,
    val providerName: String = "",
    val providerPhone: String = "",
    val status: BookingStatus = BookingStatus.ACTIVE,
    val qrCodePayload: String = "",
)

object SampleTickets {
    val sampleList = listOf(
        BookingTicket(
            ticketId = "TCK-88291",
            bookingRef = "WL-2026-PHN-REP-01",
            listingId = "101",
            title = "Phnom Penh → Siem Reap VIP Express",
            listingType = "TRIP",
            departureCity = "Phnom Penh",
            arrivalCity = "Siem Reap",
            travelDate = "2026-08-05",
            departureTime = "07:30 AM",
            seatNumber = "Seat A04, A05",
            vehicleType = "Ford Transit VIP 15-Seat",
            passengerName = "Felix Arvid",
            priceUsd = 25.0,
            providerName = "VET Express Transport",
            providerPhone = "092123456",
            status = BookingStatus.ACTIVE,
            qrCodePayload = "WANDERLUST:TICKET:WL-2026-PHN-REP-01:ACTIVE",
        ),
        BookingTicket(
            ticketId = "TCK-77102",
            bookingRef = "WL-2026-ANGKOR-SUN",
            listingId = "102",
            title = "Angkor Wat Sunrise Guided Tour",
            listingType = "TOUR",
            departureCity = "Siem Reap",
            arrivalCity = "Angkor Complex",
            travelDate = "2026-08-10",
            departureTime = "05:00 AM",
            seatNumber = "2 Adults",
            vehicleType = "Air-conditioned Minivan",
            passengerName = "Felix Arvid",
            priceUsd = 45.0,
            providerName = "Cambodia Heritage Tours",
            providerPhone = "012987654",
            status = BookingStatus.ACTIVE,
            qrCodePayload = "WANDERLUST:TICKET:WL-2026-ANGKOR-SUN:ACTIVE",
        ),
        BookingTicket(
            ticketId = "TCK-66401",
            bookingRef = "WL-2026-KMP-KEP",
            listingId = "103",
            title = "Kampot → Kep Crab Market Transfer",
            listingType = "RENTAL",
            departureCity = "Kampot",
            arrivalCity = "Kep",
            travelDate = "2026-07-15",
            departureTime = "09:00 AM",
            seatNumber = "SUV Charter",
            vehicleType = "Lexus RX300 (With Driver)",
            passengerName = "Felix Arvid",
            priceUsd = 30.0,
            providerName = "Kampot Chauffeured Car",
            providerPhone = "097555123",
            status = BookingStatus.COMPLETED,
            qrCodePayload = "WANDERLUST:TICKET:WL-2026-KMP-KEP:COMPLETED",
        ),
    )
}
