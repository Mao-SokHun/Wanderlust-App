package com.example.wanderlust.ui.components

import com.example.wanderlust.R

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderlust.data.model.AiTripPlan
import com.example.wanderlust.data.model.SampleAiItineraries
import com.example.wanderlust.locale.AppLocale
import com.example.wanderlust.util.CurrencyUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AiItineraryPlannerSection(
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val cities = listOf("Siem Reap", "Phnom Penh", "Kampot", "Mondulkiri")
    val styles = listOf("Culture & Temples", "Eco-Nature", "Beach & Chill")
    val dayOptions = listOf(1, 3, 5)

    var selectedCity by remember { mutableStateOf("Siem Reap") }
    var selectedDays by remember { mutableIntStateOf(3) }
    var selectedStyle by remember { mutableStateOf("Culture & Temples") }
    var isGenerating by remember { mutableStateOf(false) }
    var generatedPlan by remember { mutableStateOf<AiTripPlan?>(null) }

    fun generate() {
        scope.launch {
            isGenerating = true
            delay(1200)
            generatedPlan = SampleAiItineraries.generatePlan(selectedCity, selectedDays, selectedStyle)
            isGenerating = false
        }
    }

    StitchGhostCard(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
                Text(
                    if (AppLocale.isKhmer) "WanderAI — ជំនួយការរៀបចំកាលវិភាគ" else "WanderAI — Trip Itinerary Planner",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            // City Select
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    if (AppLocale.isKhmer) "ជ្រើសរើសខេត្ត៖" else "Select Destination:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    cities.forEach { city ->
                        FilterChip(
                            selected = city == selectedCity,
                            onClick = { selectedCity = city },
                            label = { Text(city, fontSize = 12.sp) },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            ),
                        )
                    }
                }
            }

            // Days & Style Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    dayOptions.forEach { d ->
                        FilterChip(
                            selected = d == selectedDays,
                            onClick = { selectedDays = d },
                            label = { Text("$d ${if (AppLocale.isKhmer) "ថ្ងៃ" else "Days"}", fontSize = 11.sp) },
                            shape = RoundedCornerShape(10.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            ),
                        )
                    }
                }

                Button(
                    onClick = { generate() },
                    enabled = !isGenerating,
                    shape = RoundedCornerShape(12.dp),
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = Color.White,
                        )
                    } else {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp).padding(end = 4.dp),
                        )
                        Text(if (AppLocale.isKhmer) "បង្កើតកាលវិភាគ" else "Generate Plan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Render Generated Plan
            generatedPlan?.let { plan ->
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    plan.days.forEach { day ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    if (AppLocale.isKhmer) day.dayTitleKh else day.dayTitleEn,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                                day.slots.forEach { slot ->
                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text(
                                            if (AppLocale.isKhmer) slot.timeSlotKh else slot.timeSlotEn,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text(
                                            "📍 ${if (AppLocale.isKhmer) slot.placeTitleKh else slot.placeTitleEn}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                        Text(
                                            if (AppLocale.isKhmer) slot.descriptionKh else slot.descriptionEn,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
