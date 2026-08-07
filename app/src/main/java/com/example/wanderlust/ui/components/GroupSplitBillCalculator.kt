package com.example.wanderlust.ui.components

import com.example.wanderlust.R

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.wanderlust.locale.AppLocale
import com.example.wanderlust.util.CurrencyUtils

@Composable
fun GroupSplitBillCalculator(
    initialTotalUsd: Double = 150.0,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var totalAmountInput by remember { mutableStateOf(initialTotalUsd.toString()) }
    val members = remember { mutableStateListOf("Member 1", "Member 2") }
    var newMemberName by remember { mutableStateOf("") }

    val totalUsd = totalAmountInput.toDoubleOrNull() ?: 0.0
    val count = members.size
    val perPersonUsd = if (count > 0) totalUsd / count else 0.0

    StitchGhostCard(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    Icons.Default.Group,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp),
                )
                Text(
                    if (AppLocale.isKhmer) "👥 ចែករំលែកការចំណាយក្រុម" else "👥 Group Split Calculator",
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
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )

            // Summary card
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            if (AppLocale.isKhmer) "តម្លៃក្នុងម្នាក់ ($count នាក់)" else "Per person ($count members)",
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
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            if (AppLocale.isKhmer) "សរុប" else "Total",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            CurrencyUtils.formatDualPrice(totalUsd),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }

            // Member List
            Text(
                if (AppLocale.isKhmer) "អ្នករួមដំណើរ" else "Group Members",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            members.forEachIndexed { index, name ->
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Box(
                            Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                name.take(1).uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            Text(name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            Text(
                                CurrencyUtils.formatDualPrice(perPersonUsd),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "👥 Wanderlust Group Split\n" +
                                        "Hi $name! Your share is: ${CurrencyUtils.formatDualPrice(perPersonUsd)}\n" +
                                        "Total for $count people: ${CurrencyUtils.formatDualPrice(totalUsd)}\n" +
                                        "Please pay via KHQR / Bakong on the Wanderlust app.",
                                    )
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share to $name"))
                            },
                            modifier = Modifier.height(32.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        ) {
                            Icon(Icons.Default.Share, null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Pay", style = MaterialTheme.typography.labelSmall)
                        }
                        if (members.size > 2) {
                            IconButton(onClick = { members.removeAt(index) }, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }

            // Add Member
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = newMemberName,
                    onValueChange = { newMemberName = it },
                    label = { Text(if (AppLocale.isKhmer) "ឈ្មោះអ្នកថ្មី" else "Add member name") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = {
                        val n = newMemberName.trim()
                        if (n.isNotBlank() && members.size < 20) {
                            members.add(n)
                            newMemberName = ""
                        }
                    },
                    enabled = newMemberName.isNotBlank() && members.size < 20,
                    modifier = Modifier.height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp),
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                }
            }

            // Share all
            Button(
                onClick = {
                    val memberLines = members.mapIndexed { i, m ->
                        "${i + 1}. $m — ${CurrencyUtils.formatDualPrice(perPersonUsd)}"
                    }.joinToString("\n")
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "👥 Wanderlust Group Split\n" +
                            "Total: ${CurrencyUtils.formatDualPrice(totalUsd)} for ${members.size} people\n\n" +
                            memberLines + "\n\nPay via KHQR / Bakong on the Wanderlust app!",
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Group Split"))
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    if (AppLocale.isKhmer) "ចែករំលែក KHQR ទៅក្រុម" else "Share Group Split to All",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

