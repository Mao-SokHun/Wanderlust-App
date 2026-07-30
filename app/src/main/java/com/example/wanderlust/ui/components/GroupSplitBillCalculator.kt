package com.example.wanderlust.ui.components

import com.example.wanderlust.R

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderlust.locale.AppLocale
import com.example.wanderlust.locale.stringApp
import com.example.wanderlust.util.CurrencyUtils

@Composable
fun GroupSplitBillCalculator(
    initialTotalUsd: Double = 150.0,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var totalAmountInput by remember { mutableStateOf(initialTotalUsd.toString()) }
    var peopleCount by remember { mutableIntStateOf(5) }

    val totalUsd = totalAmountInput.toDoubleOrNull() ?: 0.0
    val perPersonUsd = if (peopleCount > 0) totalUsd / peopleCount else 0.0

    StitchGhostCard(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    Icons.Default.Group,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
                Text(
                    if (AppLocale.isKhmer) "👥 ចែករំលែកការចំណាយក្រុម" else "👥 Group Split Bill Calculator",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            // Total Amount Input
            OutlinedTextField(
                value = totalAmountInput,
                onValueChange = { totalAmountInput = it.filter { ch -> ch.isDigit() || ch == '.' } },
                label = { Text(if (AppLocale.isKhmer) "តម្លៃសរុប ($ USD)" else "Total Booking Price ($ USD)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
            )

            // People Count Stepper
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    if (AppLocale.isKhmer) "ចំនួនអ្នករួមដំណើរ៖" else "Number of Travelers:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    IconButton(
                        onClick = { if (peopleCount > 2) peopleCount-- },
                        enabled = peopleCount > 2,
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }

                    Text(
                        "$peopleCount ${if (AppLocale.isKhmer) "នាក់" else "People"}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    IconButton(
                        onClick = { if (peopleCount < 20) peopleCount++ },
                        enabled = peopleCount < 20,
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
            }

            // Result Display Card
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        if (AppLocale.isKhmer) "តម្លៃត្រូវចំណាយក្នុងម្នាក់ៗ (" + peopleCount + " នាក់)៖" else "Split Amount Per Person ($peopleCount travelers):",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        CurrencyUtils.formatDualPrice(perPersonUsd),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            // Share Split Request Button
            Button(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "👥 Wanderlust Group Split: Total ${CurrencyUtils.formatDualPrice(totalUsd)} for $peopleCount travelers.\nEach person pays: ${CurrencyUtils.formatDualPrice(perPersonUsd)}.\nPlease send via KHQR scan!",
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Split Request"))
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp).padding(end = 6.dp))
                Text(
                    if (AppLocale.isKhmer) "ចែករំលែក KHQR ក្រុម" else "Share Group Split Request",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
