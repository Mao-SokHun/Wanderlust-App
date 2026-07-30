package com.example.wanderlust.ui.screens.tours

import com.example.wanderlust.R

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wanderlust.data.model.BookingStatus
import com.example.wanderlust.data.model.BookingTicket
import com.example.wanderlust.data.model.SampleTickets
import com.example.wanderlust.locale.AppLocale
import com.example.wanderlust.locale.stringApp
import com.example.wanderlust.locale.stringLocalized
import com.example.wanderlust.ui.components.StitchGhostCard
import com.example.wanderlust.ui.components.StickyScrollScreen
import com.example.wanderlust.ui.components.TicketQrDialog
import com.example.wanderlust.util.CurrencyUtils

/**
 * Digital Ticket & Booking History Hub for Wanderlust.
 * Manages active tickets with QR boarding codes, provider contact, dual-currency toggle, and cancellation.
 */
@Composable
fun MyBookingsScreen(
    onBack: () -> Unit,
    onOpenSaved: () -> Unit = {},
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    var activeCurrencyMode by remember { mutableStateOf(CurrencyUtils.CurrencyMode.USD) }
    var selectedQrTicket by remember { mutableStateOf<BookingTicket?>(null) }
    var cancelingTicket by remember { mutableStateOf<BookingTicket?>(null) }

    val ticketsList = remember {
        mutableStateListOf<BookingTicket>().apply {
            addAll(SampleTickets.sampleList)
        }
    }

    val activeTickets = ticketsList.filter { it.status == BookingStatus.ACTIVE }
    val completedTickets = ticketsList.filter { it.status == BookingStatus.COMPLETED }
    val canceledTickets = ticketsList.filter { it.status == BookingStatus.CANCELED }

    val currentDisplayList = when (selectedTab) {
        0 -> activeTickets
        1 -> completedTickets
        else -> canceledTickets
    }

    selectedQrTicket?.let { ticket ->
        TicketQrDialog(
            ticket = ticket,
            onDismiss = { selectedQrTicket = null },
        )
    }

    cancelingTicket?.let { ticket ->
        AlertDialog(
            onDismissRequest = { cancelingTicket = null },
            title = {
                Text(
                    if (AppLocale.isKhmer) "លុបចោលការកក់" else "Cancel Booking",
                )
            },
            text = {
                Text(
                    if (AppLocale.isKhmer) {
                        "តើអ្នកប្រាកដជាចង់លុបចោលការកក់សម្រាប់ \"${ticket.title}\" មែនទេ?"
                    } else {
                        "Are you sure you want to cancel your booking for \"${ticket.title}\"?"
                    },
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val idx = ticketsList.indexOfFirst { it.ticketId == ticket.ticketId }
                        if (idx != -1) {
                            ticketsList[idx] = ticket.copy(status = BookingStatus.CANCELED)
                        }
                        cancelingTicket = null
                    },
                ) {
                    Text(
                        stringLocalized(R.string.btn_delete, R.string.btn_delete_kh),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { cancelingTicket = null }) {
                    Text("Keep / រក្សាទុក")
                }
            },
        )
    }

    StickyScrollScreen(
        title = stringApp(R.string.profile_bookings),
        onBack = onBack,
    ) {
        // Dual Currency Quick Switcher Header
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

            // Currency Switcher Toggle Chips
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

        // Status Tabs (Active / Completed / Canceled)
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
                        "${if (AppLocale.isKhmer) "សកម្ម" else "Active"} (${activeTickets.size})",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                    )
                },
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        "${if (AppLocale.isKhmer) "រួចរាល់" else "Completed"} (${completedTickets.size})",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                    )
                },
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = {
                    Text(
                        "${if (AppLocale.isKhmer) "បោះបង់" else "Canceled"} (${canceledTickets.size})",
                        fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                    )
                },
            )
        }

        if (currentDisplayList.isEmpty()) {
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
                        if (AppLocale.isKhmer) "រុករកកន្លែង និងកក់សំបុត្ររថយន្តក្រុង ឬដំណើរកម្សាន្តនៅទីនេះ" else "Explore places and book bus trips or tours to see your tickets here.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                currentDisplayList.forEach { ticket ->
                    BookingTicketCard(
                        ticket = ticket,
                        currencyMode = activeCurrencyMode,
                        onShowQr = { selectedQrTicket = ticket },
                        onCall = {
                            if (ticket.providerPhone.isNotBlank()) {
                                context.startActivity(
                                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:${ticket.providerPhone}")),
                                )
                            }
                        },
                        onCancel = { cancelingTicket = ticket },
                    )
                }
            }
        }
    }
}

@Composable
private fun BookingTicketCard(
    ticket: BookingTicket,
    currencyMode: CurrencyUtils.CurrencyMode,
    onShowQr: () -> Unit,
    onCall: () -> Unit,
    onCancel: () -> Unit,
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
                    val icon = when (ticket.listingType) {
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
                        ticket.listingType,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                Text(
                    "REF: ${ticket.bookingRef}",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Title & Route
            Text(
                ticket.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )

            // Date, Seat, Provider Info
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    "📅 ${ticket.travelDate} · ${ticket.departureTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (ticket.seatNumber.isNotBlank()) {
                    Text(
                        "💺 ${ticket.seatNumber} (${ticket.vehicleType})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    "🏢 ${ticket.providerName}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // Footer Price & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    CurrencyUtils.formatPrice(ticket.priceUsd, mode = currencyMode),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (ticket.status == BookingStatus.ACTIVE) {
                        OutlinedButton(
                            onClick = onCancel,
                            shape = RoundedCornerShape(10.dp),
                        ) {
                            Text("Cancel", fontSize = 12.sp)
                        }

                        Button(
                            onClick = onShowQr,
                            shape = RoundedCornerShape(10.dp),
                        ) {
                            Icon(
                                Icons.Default.QrCode2,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp).padding(end = 4.dp),
                            )
                            Text("QR Ticket", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    } else if (ticket.providerPhone.isNotBlank()) {
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
}
