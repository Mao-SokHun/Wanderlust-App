package com.example.wanderlust.ui.screens.tours

import com.example.wanderlust.R

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wanderlust.data.SessionManager
import com.example.wanderlust.data.model.BookingRequest
import com.example.wanderlust.data.repository.BookingRepository
import com.example.wanderlust.locale.stringApp
import com.example.wanderlust.locale.stringLocalized
import com.example.wanderlust.ui.components.SettingsSectionTitle
import com.example.wanderlust.ui.components.StitchGhostCard
import com.example.wanderlust.ui.components.StickyScrollScreen

/** Guest: my booking requests + shortcut to Saved places. */
@Composable
fun MyBookingsScreen(
    onBack: () -> Unit,
    onOpenSaved: () -> Unit,
) {
    val repo = remember { BookingRepository() }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var requests by remember { mutableStateOf<List<BookingRequest>>(emptyList()) }

    LaunchedEffect(Unit) {
        if (!SessionManager.isLoggedIn()) {
            loading = false
            requests = emptyList()
            return@LaunchedEffect
        }
        loading = true
        repo.myRequests()
            .onSuccess { requests = it }
            .onFailure { error = it.message }
        loading = false
    }

    StickyScrollScreen(
        title = stringApp(R.string.profile_bookings),
        onBack = onBack,
    ) {
        SettingsSectionTitle(
            stringLocalized(R.string.book_my_requests_title, R.string.book_my_requests_title_kh),
        )
        when {
            loading -> CircularProgressIndicator()
            error != null -> Text(error!!, color = MaterialTheme.colorScheme.error)
            requests.isEmpty() -> Text(
                stringLocalized(R.string.book_my_requests_empty, R.string.book_my_requests_empty_kh),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            else -> {
                requests.forEach { req ->
                    StitchGhostCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    ) {
                        Column(
                            Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(req.tourTitle, fontWeight = FontWeight.SemiBold)
                            if (req.businessName.isNotBlank()) {
                                Text(
                                    req.businessName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Text(
                                bookingStatusLabel(req.status),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelMedium,
                            )
                            val meta = listOfNotNull(
                                req.travelDate?.takeIf { it.isNotBlank() },
                                "${req.guests} guests",
                                req.priceLabel.takeIf { it.isNotBlank() },
                            ).joinToString(" · ")
                            if (meta.isNotBlank()) {
                                Text(
                                    meta,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            if (req.businessReply.isNotBlank()) {
                                Text(
                                    req.businessReply,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text(
            stringLocalized(R.string.saved_plan_message, R.string.saved_plan_message_kh),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(12.dp))
        StitchGhostCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpenSaved),
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    stringApp(R.string.saved_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    stringLocalized(R.string.saved_subtitle, R.string.saved_subtitle_kh),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

@Composable
fun bookingStatusLabel(status: String): String = when (status.lowercase()) {
    "accepted" -> stringLocalized(R.string.book_status_accepted, R.string.book_status_accepted_kh)
    "declined" -> stringLocalized(R.string.book_status_declined, R.string.book_status_declined_kh)
    "contacted" -> stringLocalized(R.string.book_status_contacted, R.string.book_status_contacted_kh)
    "cancelled" -> stringLocalized(R.string.book_status_cancelled, R.string.book_status_cancelled_kh)
    else -> stringLocalized(R.string.book_status_pending, R.string.book_status_pending_kh)
}
