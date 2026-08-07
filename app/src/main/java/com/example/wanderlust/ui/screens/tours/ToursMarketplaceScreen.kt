package com.example.wanderlust.ui.screens.tours

import com.example.wanderlust.R

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.wanderlust.data.DestinationCard
import com.example.wanderlust.data.SessionManager
import com.example.wanderlust.data.repository.TourRepositoryProvider
import com.example.wanderlust.data.toDestinationCard
import com.example.wanderlust.locale.stringApp
import com.example.wanderlust.locale.stringLocalized
import com.example.wanderlust.ui.components.DestinationListCard
import com.example.wanderlust.ui.components.ScreenHeader
import com.example.wanderlust.ui.components.WanderlustBrand
import com.example.wanderlust.ui.screens.home.CompactSearchField
import kotlinx.coroutines.launch

@Composable
fun ToursMarketplaceScreen(
    onTourClick: (DestinationCard) -> Unit,
    onOpenBusinessStudio: () -> Unit,
    onSignIn: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    var listingType by remember { mutableStateOf<String?>(null) }
    var minRating by remember { mutableStateOf<Double?>(null) }
    var maxPrice by remember { mutableStateOf<Double?>(null) }
    var sort by remember { mutableStateOf("newest") }
    var radiusKm by remember { mutableStateOf<Double?>(null) }
    var tours by remember { mutableStateOf<List<DestinationCard>>(emptyList()) }
    var topTours by remember { mutableStateOf<List<DestinationCard>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val isBusiness = SessionManager.userRole == "BUSINESS" || SessionManager.isAdmin()
    val anchorLat = 11.5564
    val anchorLng = 104.9282

    fun reload() {
        scope.launch {
            loading = true
            error = null
            val useGeo = sort == "distance" || radiusKm != null
            TourRepositoryProvider.instance.getTours(
                search = query.takeIf { it.isNotBlank() },
                listingType = listingType,
                minRating = minRating,
                maxPrice = maxPrice,
                sort = sort,
                lat = if (useGeo) anchorLat else null,
                lng = if (useGeo) anchorLng else null,
                radiusKm = radiusKm,
            ).onSuccess { list ->
                tours = list.map { it.toDestinationCard() }
                loading = false
            }.onFailure {
                error = it.message
                loading = false
            }
            TourRepositoryProvider.instance.getTours(
                listingType = listingType,
                sort = "rating",
                top = 1,
                limit = 8,
                minRating = 3.5,
            ).onSuccess { list ->
                topTours = list.map { it.toDestinationCard() }
            }
        }
    }

    LaunchedEffect(listingType, minRating, maxPrice, sort, radiusKm) { reload() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            Spacer(Modifier.height(8.dp))
            WanderlustBrand()
            Spacer(Modifier.height(14.dp))
            ScreenHeader(
                title = stringLocalized(R.string.tours_market_title, R.string.tours_market_title_kh),
                subtitle = stringLocalized(R.string.tours_market_sub, R.string.tours_market_sub_kh),
                showBrand = false,
            )
            Spacer(Modifier.height(12.dp))
            CompactSearchField(
                value = query,
                onValueChange = { query = it },
                placeholder = stringLocalized(R.string.tours_market_search, R.string.tours_market_search_kh),
                onClear = {
                    query = ""
                    focusManager.clearFocus()
                    reload()
                },
                onSearch = {
                    focusManager.clearFocus()
                    reload()
                },
            )
            Spacer(Modifier.height(10.dp))

            // ── Filter row 1: Listing Type ──
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                listOf(
                    null to stringLocalized(R.string.tours_filter_all, R.string.tours_filter_all_kh),
                    "TOUR" to stringLocalized(R.string.tours_filter_tours, R.string.tours_filter_tours_kh),
                    "TRIP" to stringApp(R.string.tours_filter_trips),
                    "RENTAL" to stringApp(R.string.tours_filter_rentals),
                ).forEach { (type, label) ->
                    FilterChip(
                        selected = listingType == type,
                        onClick = { listingType = type },
                        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )
                }
            }
            Spacer(Modifier.height(4.dp))

            // ── Filter row 2: Refinements ──
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                FilterChip(
                    selected = minRating != null,
                    onClick = { minRating = if (minRating == null) 4.0 else null },
                    label = { Text(stringLocalized(R.string.tours_filter_rating, R.string.tours_filter_rating_kh), style = MaterialTheme.typography.labelMedium) },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primary, selectedLabelColor = MaterialTheme.colorScheme.onPrimary),
                )
                FilterChip(
                    selected = maxPrice != null,
                    onClick = { maxPrice = if (maxPrice == null) 50.0 else null },
                    label = { Text(stringLocalized(R.string.tours_price_under_50, R.string.tours_price_under_50_kh), style = MaterialTheme.typography.labelMedium) },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primary, selectedLabelColor = MaterialTheme.colorScheme.onPrimary),
                )
                FilterChip(
                    selected = radiusKm != null,
                    onClick = { radiusKm = if (radiusKm == null) 25.0 else null },
                    label = { Text(stringLocalized(R.string.tours_radius_25, R.string.tours_radius_25_kh), style = MaterialTheme.typography.labelMedium) },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primary, selectedLabelColor = MaterialTheme.colorScheme.onPrimary),
                )
            }
            Spacer(Modifier.height(4.dp))

            // ── Sort row ──
            Row(
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                listOf(
                    "newest" to stringLocalized(R.string.tours_sort_newest, R.string.tours_sort_newest_kh),
                    "rating" to stringLocalized(R.string.tours_sort_rating, R.string.tours_sort_rating_kh),
                    "price_asc" to stringLocalized(R.string.tours_sort_price_low, R.string.tours_sort_price_low_kh),
                    "price_desc" to stringLocalized(R.string.tours_sort_price_high, R.string.tours_sort_price_high_kh),
                    "distance" to stringLocalized(R.string.tours_sort_near, R.string.tours_sort_near_kh),
                ).forEach { (value, label) ->
                    FilterChip(
                        selected = sort == value,
                        onClick = { sort = value },
                        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                            selectedLabelColor = MaterialTheme.colorScheme.onTertiary,
                        ),
                    )
                }
            }

            if (isBusiness) {
                Spacer(Modifier.height(10.dp))
                Button(onClick = onOpenBusinessStudio, modifier = Modifier.fillMaxWidth()) {
                    Text(stringLocalized(R.string.business_open_studio, R.string.business_open_studio_kh))
                }
            }

            // ── TOP RATED: Large Hero Cards ──────────────────────────────
            if (topTours.isNotEmpty()) {
                Spacer(Modifier.height(20.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        stringLocalized(R.string.tours_top_title, R.string.tours_top_title_kh),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Surface(
                        color = Color(0xFFF59E0B).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Row(
                            Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Icon(Icons.Default.Star, null, tint = Color(0xFFF59E0B), modifier = Modifier.height(14.dp))
                            Text(
                                "Top Rated",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFB45309),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(end = 4.dp),
                ) {
                    items(topTours, key = { "top-${it.id}" }) { dest ->
                        TopRatedHeroCard(destination = dest, onClick = { onTourClick(dest) })
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // ── All Listings header ──────────────────────────────────────
            if (!loading || tours.isNotEmpty()) {
                Text(
                    "All Listings",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.height(4.dp))
            }

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 8.dp))
            }
            if (!loading && tours.isEmpty() && error == null) {
                Text(
                    stringLocalized(R.string.tours_market_empty, R.string.tours_market_empty_kh),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 12.dp),
                )
            }
        }
        items(tours, key = { it.id }) { dest ->
            DestinationListCard(destination = dest, onClick = { onTourClick(dest) })
        }
    }
}

/** Large 300x220dp hero card used in Top Rated horizontal carousel */
@Composable
private fun TopRatedHeroCard(
    destination: DestinationCard,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 6.dp,
        modifier = Modifier
            .width(300.dp)
            .height(220.dp)
            .clickable(onClick = onClick),
    ) {
        Box {
            AsyncImage(
                model = destination.imageUrl,
                contentDescription = destination.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                            startY = 60f,
                        ),
                    ),
            )
            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp),
            ) {
                // Rating chip
                destination.rating?.let { rating ->
                    Surface(
                        color = Color(0xFFF59E0B).copy(alpha = 0.92f),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Row(
                            Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                        ) {
                            Icon(Icons.Default.Star, null, tint = Color.White, modifier = Modifier.height(12.dp))
                            Text("%.1f".format(rating), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                }
                Text(
                    destination.title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (destination.location.isNotBlank()) {
                    Text(
                        destination.location,
                        color = Color.White.copy(alpha = 0.80f),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                destination.priceUsd?.let { price ->
                    Text(
                        "$%.0f".format(price),
                        color = Color(0xFF6EE7B7),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}
