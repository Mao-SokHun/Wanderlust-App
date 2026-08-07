package com.example.wanderlust.ui.screens.tours

import com.example.wanderlust.R

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderlust.data.model.UserBooking
import com.example.wanderlust.data.repository.BookingRepository
import com.example.wanderlust.locale.AppLocale
import com.example.wanderlust.locale.stringApp
import com.example.wanderlust.ui.components.StitchGhostCard
import com.example.wanderlust.ui.components.StickyScrollScreen
import com.example.wanderlust.util.CurrencyUtils
import kotlinx.coroutines.launch

/**
 * My Bookings & Tickets screen — loads real bookings from the API.
 */
@Composable
fun MyBookingsScreen(
    onBack: () -> Unit,
    onOpenSaved: () -> Unit = {},
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repo = remember { BookingRepository() }

    var selectedTab by remember { mutableIntStateOf(0) }
    var activeCurrencyMode by remember { mutableStateOf(CurrencyUtils.CurrencyMode.USD) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val bookings = remember { mutableStateListOf<UserBooking>() }

    fun loadBookings() {
        scope.launch {
            isLoading = true
            errorMsg = null
            repo.getMyBookings()
                .onSuccess { list ->
                    bookings.clear()
                    bookings.addAll(list)
                }
                .onFailure { e ->
                    errorMsg = e.message ?: "Failed to load bookings"
                }
            isLoading = false
        }
    }

    LaunchedEffect(Unit) { loadBookings() }

    // Filter by status tab
    val activeBookings = bookings.filter { it.status in listOf("PENDING", "CONFIRMED", "ACTIVE") }
    val completedBookings = bookings.filter { it.status == "COMPLETED" }
    val canceledBookings = bookings.filter { it.status in listOf("CANCELLED", "CANCELED") }

    val currentList = when (selectedTab) {
        0 -> activeBookings
        1 -> completedBookings
        else -> canceledBookings
    }

    StickyScrollScreen(
        title = stringApp(R.string.profile_bookings),
        onBack = onBack,
        headerTrailing = {
            if (!isLoading) {
                IconButton(onClick = { loadBookings() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }
        },
    ) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                if (AppLocale.isKhmer) "សំបុត្រ និងប្រវត្តិការកក់" else "Tickets & Reservations",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(
                    selected = activeCurrencyMode == CurrencyUtils.CurrencyMode.USD,
                    onClick = {
                        activeCurrencyMode = CurrencyUtils.CurrencyMode.USD
                        CurrencyUtils.activeCurrency = CurrencyUtils.CurrencyMode.USD
                    },
                    label = { Text("$ USD") },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
                FilterChip(
                    selected = activeCurrencyMode == CurrencyUtils.CurrencyMode.KHR,
                    onClick = {
                        activeCurrencyMode = CurrencyUtils.CurrencyMode.KHR
                        CurrencyUtils.activeCurrency = CurrencyUtils.CurrencyMode.KHR
                    },
                    label = { Text("៛ KHR") },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
            }
        }

        // Status Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .padding(bottom = 14.dp),
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        "${if (AppLocale.isKhmer) "សកម្ម" else "Active"} (${activeBookings.size})",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                    )
                },
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        "${if (AppLocale.isKhmer) "រួចរាល់" else "Completed"} (${completedBookings.size})",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                    )
                },
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = {
                    Text(
                        "${if (AppLocale.isKhmer) "បោះបង់" else "Canceled"} (${canceledBookings.size})",
                        fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                    )
                },
            )
        }

        // Loading
        if (isLoading) {
            Row(Modifier.fillMaxWidth().padding(vertical = 32.dp), horizontalArrangement = Arrangement.Center) {
                CircularProgressIndicator()
            }
            return@StickyScrollScreen
        }

        // Error
        errorMsg?.let { msg ->
            StitchGhostCard(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(msg, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { loadBookings() }) { Text("Retry") }
                }
            }
            return@StickyScrollScreen
        }

        // Empty state
        if (currentList.isEmpty()) {
            StitchGhostCard(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        Icons.Default.ConfirmationNumber,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    )
                    Text(
                        if (AppLocale.isKhmer) "មិនទាន់មានសំបុត្រទេ" else "No tickets in this category",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        if (AppLocale.isKhmer) "រុករកកន្លែង និងកក់ដំណើរកម្សាន្ត ដើម្បីឃើញ​សំបុត្ររបស់អ្នក" else "Browse tours and make a booking to see your tickets here.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                currentList.forEach { booking ->
                    BookingCard(
                        booking = booking,
                        currencyMode = activeCurrencyMode,
                        onCall = {
                            if (booking.providerPhone.isNotBlank()) {
                                context.startActivity(
                                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:${booking.providerPhone}")),
                                )
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun BookingCard(
    booking: UserBooking,
    currencyMode: CurrencyUtils.CurrencyMode,
    onCall: () -> Unit,
) {
    StitchGhostCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header Row: Type Badge + Ref Code + Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    val icon = when (booking.listingType) {
                        "RENTAL" -> Icons.Default.DirectionsCar
                        "TOUR" -> Icons.Default.Map
                        else -> Icons.Default.DirectionsBus
                    }
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        booking.listingType,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                // Status chip
                val statusColor = when (booking.status) {
                    "CONFIRMED" -> MaterialTheme.colorScheme.primary
                    "COMPLETED" -> MaterialTheme.colorScheme.tertiary
                    "CANCELLED", "CANCELED" -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
                Text(
                    booking.status,
                    style = MaterialTheme.typography.labelSmall,
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                )
            }

            // Ref
            Text(
                "REF: ${booking.bookingRef}",
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            // Title
            Text(
                booking.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )

            // Location / Date / Seat / Provider Info
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                if (booking.location.isNotBlank()) {
                    Text(
                        "📍 ${booking.location}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (booking.travelDate.isNotBlank()) {
                    Text(
                        "📅 ${booking.travelDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (booking.seatNumber.isNotBlank()) {
                    Text(
                        "💺 ${booking.seatNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (booking.passengerName.isNotBlank()) {
                    Text(
                        "👤 ${booking.passengerName}",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                if (booking.providerName.isNotBlank()) {
                    Text(
                        "🏢 ${booking.providerName}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // Footer Price & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    if (booking.priceLabel.isNotBlank()) booking.priceLabel
                    else CurrencyUtils.formatPrice(booking.priceUsd, mode = currencyMode),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )

                if (booking.providerPhone.isNotBlank() && booking.status in listOf("PENDING", "CONFIRMED", "ACTIVE")) {
                    OutlinedButton(
                        onClick = onCall,
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Icon(
                            Icons.Default.Call,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp).padding(end = 4.dp),
                        )
                        Text("Call", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
