package com.example.wanderlust.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Single chat message in a direct thread between traveler and business account. */
data class ChatMessage(
    val id: String = System.currentTimeMillis().toString(),
    val senderId: String,
    val senderName: String,
    val senderRole: String = "USER",
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromUser: Boolean = true,
) {
    val formattedTime: String
        get() {
            val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
}

data class ChatApiMessage(
    val id: Int,
    val sender_id: Int,
    val receiver_id: Int,
    val message: String,
    val created_at: String
)

data class ChatApiSendRequest(
    val message: String
)

/** Quick inquiry preset for 1-tap fast messaging. */
data class QuickInquiryChip(
    val id: String,
    val textEn: String,
    val textKh: String,
)

/** Context banner info for the listing being discussed. */
data class ListingInquiryContext(
    val listingId: String,
    val title: String,
    val priceLabel: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val location: String = "",
)

object QuickInquiryPresets {
    val defaultList = listOf(
        QuickInquiryChip(
            id = "availability",
            textEn = "Is this available on my dates?",
            textKh = "តើមានកន្លែងទំនេរក្នុងថ្ងៃនេះទេ?",
        ),
        QuickInquiryChip(
            id = "inclusions",
            textEn = "What is included in this price?",
            textKh = "តើសេវានេះរាប់បញ្ចូលអ្វីខ្លះ?",
        ),
        QuickInquiryChip(
            id = "group_discount",
            textEn = "Do you offer group discounts?",
            textKh = "តើមានការបញ្ចុះតម្លៃសម្រាប់ក្រុមទេ?",
        ),
        QuickInquiryChip(
            id = "pickup",
            textEn = "Where is the pickup location?",
            textKh = "តើទីតាំងទទួលភ្ញៀវនៅឯណា?",
        ),
    )
}
