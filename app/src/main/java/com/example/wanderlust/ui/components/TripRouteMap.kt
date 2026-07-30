package com.example.wanderlust.ui.components

import com.example.wanderlust.R

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wanderlust.locale.AppLocale
import com.example.wanderlust.locale.stringApp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

data class RestStopPin(
    val title: String,
    val snippet: String,
    val latLng: LatLng,
)

/**
 * Interactive Bus & Trip Route Map component for Google Maps.
 * Draws highway polylines connecting departure & arrival terminals with highway rest stop markers.
 */
@Composable
fun TripRouteMap(
    departureCity: String = "Phnom Penh",
    arrivalCity: String = "Siem Reap",
    departureLatLng: LatLng = LatLng(11.5564, 104.9282), // Phnom Penh
    arrivalLatLng: LatLng = LatLng(13.3633, 103.8564),   // Siem Reap
    modifier: Modifier = Modifier,
) {
    // Highway rest stop midpoint (Kampong Thom highway rest stop along NH6)
    val midLat = (departureLatLng.latitude + arrivalLatLng.latitude) / 2
    val midLng = (departureLatLng.longitude + arrivalLatLng.longitude) / 2
    val restStopLatLng = LatLng(midLat, midLng)

    val routePoints = listOf(
        departureLatLng,
        LatLng(departureLatLng.latitude + 0.5, departureLatLng.longitude - 0.1),
        restStopLatLng,
        LatLng(arrivalLatLng.latitude - 0.4, arrivalLatLng.longitude + 0.1),
        arrivalLatLng,
    )

    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(restStopLatLng, 7.8f)
    }

    LaunchedEffect(departureLatLng, arrivalLatLng) {
        cameraState.position = CameraPosition.fromLatLngZoom(restStopLatLng, 7.8f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(16.dp)),
    ) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraState,
        ) {
            // Polyline connecting route
            Polyline(
                points = routePoints,
                color = MaterialTheme.colorScheme.primary,
                width = 8f,
            )

            // Departure Terminal Marker
            Marker(
                state = rememberMarkerState(position = departureLatLng),
                title = "Departure: $departureCity",
                snippet = "Terminal & Boarding Point",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
            )

            // Rest Stop Marker
            Marker(
                state = rememberMarkerState(position = restStopLatLng),
                title = "Highway Rest Stop",
                snippet = "Food, Restrooms, & Refreshments (20 min)",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
            )

            // Arrival Terminal Marker
            Marker(
                state = rememberMarkerState(position = arrivalLatLng),
                title = "Arrival: $arrivalCity",
                snippet = "Destination Station",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
            )
        }

        // Overlay Route Badge
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp),
        ) {
            Text(
                "🛣️ $departureCity → $arrivalCity (NH6 Highway)",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            )
        }
    }
}
