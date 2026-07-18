package com.example.wanderlust.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlust.data.DestinationCard
import com.example.wanderlust.data.SessionManager
import com.example.wanderlust.data.model.Tour
import com.example.wanderlust.data.repository.BookingRepository
import com.example.wanderlust.data.repository.FavoriteRepository
import com.example.wanderlust.data.repository.TourRepositoryProvider
import com.example.wanderlust.util.Validation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TourDetailViewModel(
    private val favoriteRepository: FavoriteRepository = FavoriteRepository(),
    private val bookingRepository: BookingRepository = BookingRepository(),
) : ViewModel() {

    var isSaved by mutableStateOf(false)
        private set
    var saveMessage by mutableStateOf<String?>(null)
        private set
    var weatherText by mutableStateOf("Loading weather...")
        private set
    var displayRating by mutableStateOf(0.0)
        private set
    var displayRatingCount by mutableIntStateOf(0)
        private set
    var myRating by mutableIntStateOf(0)
        private set
    var rateMessage by mutableStateOf<String?>(null)
        private set
    var ratingBusy by mutableStateOf(false)
        private set
    var bookingBusy by mutableStateOf(false)
        private set
    var bookingMessage by mutableStateOf<String?>(null)
        private set

    fun clearSaveMessage() {
        saveMessage = null
    }

    fun clearRateMessage() {
        rateMessage = null
    }

    fun clearBookingMessage() {
        bookingMessage = null
    }

    fun initRating(destination: DestinationCard) {
        displayRating = destination.rating
        displayRatingCount = destination.ratingCount
        myRating = 0
        if (!destination.id.matches(Regex("^\\d+$"))) return
        if (!SessionManager.isLoggedIn()) return
        viewModelScope.launch {
            TourRepositoryProvider.instance.getMyRating(destination.id)
                .onSuccess { myRating = it.myRating ?: 0 }
        }
    }

    fun selectStars(stars: Int) {
        myRating = stars.coerceIn(1, 5)
    }

    fun submitRating(tourId: String) {
        if (!tourId.matches(Regex("^\\d+$"))) {
            rateMessage = "error"
            return
        }
        if (!SessionManager.isLoggedIn()) {
            rateMessage = "signin"
            return
        }
        if (myRating < 1) return
        if (Validation.requireStars(myRating) != null) return
        viewModelScope.launch {
            ratingBusy = true
            TourRepositoryProvider.instance.rateTour(tourId, myRating)
                .onSuccess {
                    displayRating = it.rating
                    displayRatingCount = it.ratingCount
                    myRating = it.myRating ?: myRating
                    rateMessage = "ok"
                }
                .onFailure { rateMessage = "error" }
            ratingBusy = false
        }
    }

    /** Guest: submit request-to-book for this listing. */
    fun submitBookingRequest(
        tourId: String,
        travelDate: String,
        guests: Int,
        message: String,
        guestPhone: String,
    ) {
        if (!tourId.matches(Regex("^\\d+$"))) {
            bookingMessage = "error"
            return
        }
        if (!SessionManager.isLoggedIn()) {
            bookingMessage = "signin"
            return
        }
        viewModelScope.launch {
            bookingBusy = true
            bookingRepository.createRequest(
                tourId = tourId,
                travelDate = travelDate.trim().ifBlank { null },
                guests = guests,
                message = message.trim(),
                guestPhone = guestPhone.trim(),
            ).onSuccess {
                bookingMessage = "ok"
            }.onFailure {
                bookingMessage = it.message?.takeIf { m -> m.isNotBlank() } ?: "error"
            }
            bookingBusy = false
        }
    }

    fun loadWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val weather = withContext(Dispatchers.IO) {
                com.example.wanderlust.data.repository.WeatherRepository()
                    .getCurrentWeather(latitude, longitude)
                    .getOrNull()
            }
            weatherText = if (weather != null) {
                "${weather.temperature.toInt()}°C • ${com.example.wanderlust.data.weatherLabel(weather.weathercode)}"
            } else {
                "—"
            }
        }
    }

    fun loadSavedState(tourId: String, title: String) {
        viewModelScope.launch {
            isSaved = favoriteRepository.isFavorite(tourId, title)
        }
    }

    fun toggleSave(destination: DestinationCard) {
        val tour = Tour(
            id = destination.id,
            title = destination.title,
            description = destination.description,
            category = destination.category,
            rating = destination.rating,
        )
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(tour)
                .onSuccess { saved ->
                    isSaved = saved
                    saveMessage = if (saved) "saved" else "removed"
                }
                .onFailure {
                    saveMessage = "error"
                }
        }
    }
}
