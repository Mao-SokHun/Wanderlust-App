package com.example.wanderlust.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class NotificationType {
    BOOKING,
    CHAT,
    SYSTEM,
}

data class AppNotification(
    val id: String,
    val titleEn: String,
    val titleKh: String,
    val messageEn: String,
    val messageKh: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: NotificationType = NotificationType.SYSTEM,
    val read: Boolean = false,
) {
    val formattedTime: String
        get() {
            val sdf = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
}

object SampleNotifications {
    val sampleList = listOf(
        AppNotification(
            id = "notif-01",
            titleEn = "Bus Departure Reminder",
            titleKh = "ការរំលឹកអំពីការចេញដំណើរកម្សាន្ត",
            messageEn = "Your VIP Express bus to Siem Reap leaves in 2 hours at 07:30 AM. Have your QR ticket ready!",
            messageKh = "រថយន្តក្រុង VIP ទៅសៀមរាប នឹងចេញដំណើរក្នុងរយៈពេល ២ ម៉ោងទៀត (ម៉ោង ៧:៣០ ព្រឹក)។ សូមរៀបចំសំបុត្រ QR!",
            timestamp = System.currentTimeMillis() - 3600000 * 2,
            type = NotificationType.BOOKING,
            read = false,
        ),
        AppNotification(
            id = "notif-02",
            titleEn = "New Host Message",
            titleKh = "សារថ្មីពីម្ចាស់អាជីវកម្ម",
            messageEn = "Cambodia Heritage Tours replied: 'Yes, we have sunrise spots available for tomorrow!'",
            messageKh = "Cambodia Heritage Tours បានឆ្លើយតប៖ 'បាទ/ចាស យើងមានកន្លែងទំនេរសម្រាប់ថ្ងៃរះថ្ងៃស្អែក!'",
            timestamp = System.currentTimeMillis() - 3600000 * 5,
            type = NotificationType.CHAT,
            read = false,
        ),
        AppNotification(
            id = "notif-03",
            titleEn = "Booking Confirmed!",
            titleKh = "ការកក់ត្រូវបានបញ្ជាក់!",
            messageEn = "Your car rental reservation WL-2026-KMP-KEP for Kampot is confirmed.",
            messageKh = "ការកក់រថយន្ត WL-2026-KMP-KEP សម្រាប់កំពត ត្រូវបានបញ្ជាក់ដោយជោគជ័យ។",
            timestamp = System.currentTimeMillis() - 3600000 * 24,
            type = NotificationType.BOOKING,
            read = true,
        ),
        AppNotification(
            id = "notif-04",
            titleEn = "Welcome to Wanderlust 🌿",
            titleKh = "សូមស្វាគមន៍មកកាន់ Wanderlust 🌿",
            messageEn = "Explore top destinations, book trips, and discover local places across Cambodia.",
            messageKh = "រុករកទីតាំងកំពូលៗ កក់សំបុត្រធ្វើដំណើរ និងស្វែងរកកន្លែងក្នុងស្រុកជុំវិញប្រទេសកម្ពុជា។",
            timestamp = System.currentTimeMillis() - 3600000 * 48,
            type = NotificationType.SYSTEM,
            read = true,
        ),
    )
}
