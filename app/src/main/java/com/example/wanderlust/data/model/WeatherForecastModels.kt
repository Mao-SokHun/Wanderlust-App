package com.example.wanderlust.data.model

data class DailyForecast(
    val dayName: String,
    val date: String,
    val iconEmoji: String = "☀️",
    val conditionEn: String = "Sunny",
    val conditionKh: String = "ថ្ងៃស្រឡះ",
    val highTempC: Int = 32,
    val lowTempC: Int = 24,
    val humidityPercent: Int = 65,
)

data class CityWeatherForecast(
    val cityName: String,
    val currentTempC: Int = 30,
    val currentConditionEn: String = "Partly Cloudy",
    val currentConditionKh: String = "មានពពកខ្លះ",
    val dailyList: List<DailyForecast> = emptyList(),
)

object SampleWeatherForecasts {
    val forecastsMap = mapOf(
        "Siem Reap" to CityWeatherForecast(
            cityName = "Siem Reap",
            currentTempC = 31,
            currentConditionEn = "Partly Cloudy",
            currentConditionKh = "មានពពកខ្លះ",
            dailyList = listOf(
                DailyForecast("Mon", "Aug 3", "☀️", "Sunny", "ថ្ងៃស្រឡះ", 33, 25, 60),
                DailyForecast("Tue", "Aug 4", "⛅", "Partly Cloudy", "មានពពកខ្លះ", 32, 24, 65),
                DailyForecast("Wed", "Aug 5", "🌧️", "Light Rain", "ភ្លៀងរ៉ឹមៗ", 29, 23, 80),
                DailyForecast("Thu", "Aug 6", "⛅", "Partly Cloudy", "មានពពកខ្លះ", 31, 24, 70),
                DailyForecast("Fri", "Aug 7", "☀️", "Sunny", "ថ្ងៃស្រឡះ", 34, 25, 58),
            ),
        ),
        "Phnom Penh" to CityWeatherForecast(
            cityName = "Phnom Penh",
            currentTempC = 33,
            currentConditionEn = "Sunny",
            currentConditionKh = "ថ្ងៃស្រឡះ",
            dailyList = listOf(
                DailyForecast("Mon", "Aug 3", "☀️", "Sunny", "ថ្ងៃស្រឡះ", 34, 26, 62),
                DailyForecast("Tue", "Aug 4", "☀️", "Sunny", "ថ្ងៃស្រឡះ", 35, 26, 60),
                DailyForecast("Wed", "Aug 5", "⛅", "Partly Cloudy", "មានពពកខ្លះ", 33, 25, 68),
                DailyForecast("Thu", "Aug 6", "🌧️", "Afternoon Rain", "ភ្លៀងល្ងាច", 30, 24, 82),
                DailyForecast("Fri", "Aug 7", "☀️", "Sunny", "ថ្ងៃស្រឡះ", 33, 25, 65),
            ),
        ),
        "Kampot" to CityWeatherForecast(
            cityName = "Kampot",
            currentTempC = 29,
            currentConditionEn = "Pleasant Breeze",
            currentConditionKh = "ខ្យល់ជំនោរត្រជាក់",
            dailyList = listOf(
                DailyForecast("Mon", "Aug 3", "⛅", "Partly Cloudy", "មានពពកខ្លះ", 30, 23, 75),
                DailyForecast("Tue", "Aug 4", "🌧️", "Coastal Shower", "ភ្លៀងមាត់សមុទ្រ", 28, 22, 85),
                DailyForecast("Wed", "Aug 5", "☀️", "Sunny", "ថ្ងៃស្រឡះ", 31, 23, 70),
                DailyForecast("Thu", "Aug 6", "☀️", "Clear Sky", "មេឃស្រឡះល្អ", 32, 24, 68),
                DailyForecast("Fri", "Aug 7", "⛅", "Partly Cloudy", "មានពពកខ្លះ", 30, 23, 74),
            ),
        ),
        "Mondulkiri" to CityWeatherForecast(
            cityName = "Mondulkiri",
            currentTempC = 24,
            currentConditionEn = "Cool Highlands",
            currentConditionKh = "អាកាសធាតុត្រជាក់",
            dailyList = listOf(
                DailyForecast("Mon", "Aug 3", "⛅", "Cool Breeze", "ខ្យល់ត្រជាក់", 25, 18, 70),
                DailyForecast("Tue", "Aug 4", "☀️", "Fresh Sunshine", "ពន្លឺថ្ងៃស្រស់ថ្លា", 26, 17, 65),
                DailyForecast("Wed", "Aug 5", "🌧️", "Mountain Mist", "អ័ព្ទភ្នំ", 22, 17, 90),
                DailyForecast("Thu", "Aug 6", "⛅", "Cool & Clear", "ត្រជាក់ស្រឡះ", 24, 18, 72),
                DailyForecast("Fri", "Aug 7", "☀️", "Sunny Highlands", "ថ្ងៃស្រឡះលើភ្នំ", 26, 18, 68),
            ),
        ),
    )
}
