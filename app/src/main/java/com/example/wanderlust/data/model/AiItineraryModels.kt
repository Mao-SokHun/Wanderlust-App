package com.example.wanderlust.data.model

data class AiActivitySlot(
    val timeSlotEn: String,
    val timeSlotKh: String,
    val placeTitleEn: String,
    val placeTitleKh: String,
    val descriptionEn: String,
    val descriptionKh: String,
    val approxCostUsd: Double = 0.0,
)

data class AiDaySchedule(
    val dayNumber: Int,
    val dayTitleEn: String,
    val dayTitleKh: String,
    val slots: List<AiActivitySlot> = emptyList(),
)

data class AiTripPlan(
    val city: String,
    val daysCount: Int,
    val travelStyle: String,
    val days: List<AiDaySchedule> = emptyList(),
)

object SampleAiItineraries {
    fun generatePlan(city: String, days: Int, style: String): AiTripPlan {
        val daysList = (1..days.coerceIn(1, 5)).map { dayNum ->
            AiDaySchedule(
                dayNumber = dayNum,
                dayTitleEn = "Day $dayNum — $style Highlights",
                dayTitleKh = "ថ្ងៃទី $dayNum — ទីតាំងសំខាន់ៗ $style",
                slots = listOf(
                    AiActivitySlot(
                        timeSlotEn = "07:00 AM — Morning",
                        timeSlotKh = "ម៉ោង ០៧:០០ ព្រឹក — ព្រឹកព្រលឹម",
                        placeTitleEn = if (city == "Siem Reap") "Angkor Wat Sunrise" else "$city Central Landmark",
                        placeTitleKh = if (city == "Siem Reap") "ទស្សនាថ្ងៃរះនៅអង្គរវត្ត" else "ទីតាំងប្រវត្តិសាស្ត្រ $city",
                        descriptionEn = "Explore iconic views and local breakfast spots.",
                        descriptionKh = "ទស្សនាទិដ្ឋភាពដ៏ត្រកាល និងអាហារពេលព្រឹកក្នុងស្រុក។",
                        approxCostUsd = 15.0,
                    ),
                    AiActivitySlot(
                        timeSlotEn = "01:30 PM — Afternoon",
                        timeSlotKh = "ម៉ោង ០១:៣០ រសៀល — រសៀល",
                        placeTitleEn = if (city == "Siem Reap") "Bayon Temple & Ta Prohm" else "$city Cultural Center",
                        placeTitleKh = if (city == "Siem Reap") "ប្រាសាទបាយ័ន និងប្រាសាទតាព្រហ្ម" else "មជ្ឈមណ្ឌលវប្បធម៌ $city",
                        descriptionEn = "Discover ancient stone carvings and nature trails.",
                        descriptionKh = "ស្វែងយល់អំពីចម្លាក់បុរាណ និងផ្លូវដើរធម្មជាតិ។",
                        approxCostUsd = 20.0,
                    ),
                    AiActivitySlot(
                        timeSlotEn = "06:30 PM — Evening",
                        timeSlotKh = "ម៉ោង ០៦:៣០ ល្ងាច — ព្រលប់",
                        placeTitleEn = "$city Night Market & Local Dining",
                        placeTitleKh = "ផ្សាររាត្រី និងអាហារពេលល្ងាច $city",
                        descriptionEn = "Enjoy authentic Khmer food and souvenir shopping.",
                        descriptionKh = "រីករាយជាមួយហាងអាហារខ្មែរ និងទិញកាដូអនុស្សាវរីយ៍។",
                        approxCostUsd = 12.0,
                    ),
                ),
            )
        }
        return AiTripPlan(city = city, daysCount = days, travelStyle = style, days = daysList)
    }
}
