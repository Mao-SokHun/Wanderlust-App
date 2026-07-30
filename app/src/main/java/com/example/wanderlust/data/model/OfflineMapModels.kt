package com.example.wanderlust.data.model

data class OfflineMapRegion(
    val id: String,
    val nameEn: String,
    val nameKh: String,
    val sizeMb: Int,
    val landmarksCount: Int,
    val downloaded: Boolean = false,
    val progressPercent: Float = 0f,
)

object SampleOfflineMaps {
    val sampleList = listOf(
        OfflineMapRegion("map-rep", "Siem Reap & Angkor Complex", "សៀមរាប និងតំបន់អង្គរ", 45, 120, downloaded = true),
        OfflineMapRegion("map-pnh", "Phnom Penh Capital City", "រាជធានីភ្នំពេញ", 38, 95, downloaded = false),
        OfflineMapRegion("map-kmp", "Kampot & Kep Coastal Region", "ខេត្តកំពត និងកែប", 28, 64, downloaded = false),
        OfflineMapRegion("map-mdk", "Mondulkiri Highlands & Waterfalls", "ខេត្តមណ្ឌលគិរី", 32, 48, downloaded = false),
    )
}
