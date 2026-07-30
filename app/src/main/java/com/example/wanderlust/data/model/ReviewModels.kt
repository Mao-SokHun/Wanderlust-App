package com.example.wanderlust.data.model

data class TravelerReview(
    val id: String,
    val authorName: String,
    val authorAvatar: String = "",
    val rating: Int = 5,
    val date: String = "",
    val comment: String = "",
    val isVerifiedTraveler: Boolean = true,
    val photoUrls: List<String> = emptyList(),
    val helpfulCount: Int = 0,
)

object SampleReviews {
    val sampleList = listOf(
        TravelerReview(
            id = "rev-01",
            authorName = "Sokha Chen",
            rating = 5,
            date = "July 24, 2026",
            comment = "Amazing sunrise trip at Angkor Wat! The VIP van was very clean with strong A/C, and our local guide was super knowledgeable.",
            isVerifiedTraveler = true,
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=500",
                "https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=500",
            ),
            helpfulCount = 14,
        ),
        TravelerReview(
            id = "rev-02",
            authorName = "Maya K. Lee",
            rating = 5,
            date = "July 18, 2026",
            comment = "Smooth bus transfer from Phnom Penh to Siem Reap. Departed right on time at 7:30 AM. Highly recommended!",
            isVerifiedTraveler = true,
            photoUrls = emptyList(),
            helpfulCount = 9,
        ),
        TravelerReview(
            id = "rev-03",
            authorName = "Felix Arvid",
            rating = 4,
            date = "July 10, 2026",
            comment = "Rented the SUV for a Kampot & Kep trip. Great driver, comfortable seats, very reliable service.",
            isVerifiedTraveler = true,
            photoUrls = listOf(
                "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=500",
            ),
            helpfulCount = 6,
        ),
    )
}
