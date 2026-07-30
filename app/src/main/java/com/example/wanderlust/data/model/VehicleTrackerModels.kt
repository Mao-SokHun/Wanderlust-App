package com.example.wanderlust.data.model

data class LiveVehicleStatus(
    val vehicleId: String,
    val busOperator: String,
    val plateNumber: String,
    val originCity: String,
    val destinationCity: String,
    val currentSpeedKmH: Int = 75,
    val etaMinutes: Int = 24,
    val nextRestStopEn: String = "Kampong Thom Rest Stop (15 km)",
    val nextRestStopKh: String = "កន្លែងឈប់សម្រាក ខេត្តកំពង់ធំ (១៥ គ.ម)",
    val progressPercent: Float = 0.68f,
)

object SampleVehicleStatus {
    val sampleBus = LiveVehicleStatus(
        vehicleId = "bus-wl-998",
        busOperator = "Larryta Express VIP",
        plateNumber = "Phnom Penh 2BC-8899",
        originCity = "Phnom Penh",
        destinationCity = "Siem Reap",
        currentSpeedKmH = 75,
        etaMinutes = 24,
        nextRestStopEn = "Kampong Thom Rest Stop (15 km)",
        nextRestStopKh = "កន្លែងឈប់សម្រាក ខេត្តកំពង់ធំ (១៥ គ.ម)",
        progressPercent = 0.68f,
    )
}
