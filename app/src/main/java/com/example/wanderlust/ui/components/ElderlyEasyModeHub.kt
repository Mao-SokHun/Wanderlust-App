package com.example.wanderlust.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderlust.locale.AppLocale

@Composable
fun ElderlyEasyModeHub(
    onBookBusClick: () -> Unit = {},
    onExplorePlacesClick: () -> Unit = {},
    onMyTicketsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Senior-Friendly Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Default.TouchApp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                    Column {
                        Text(
                            if (AppLocale.isKhmer) "👵 របៀបងាយស្រួល (Easy Mode)" else "👵 Senior Easy Access",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Text(
                            if (AppLocale.isKhmer) "អក្សរធំៗ ចុចតែ១ឃ្លីក ងាយស្រួលប្រកដ" else "Large buttons & simple 1-tap actions",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        )
                    }
                }
            }

            // 4 Large 1-Tap Action Tiles Grid (2x2)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Action 1: Book Bus
                EasyActionButton(
                    icon = Icons.Default.DirectionsBus,
                    labelKh = "កក់សំបុត្រឡាន",
                    labelEn = "Book Bus Ticket",
                    containerColor = Color(0xFF059669),
                    onClick = onBookBusClick,
                    modifier = Modifier.weight(1f),
                )

                // Action 2: Explore Places
                EasyActionButton(
                    icon = Icons.Default.Explore,
                    labelKh = "មើលកន្លែងដើរលេង",
                    labelEn = "Explore Places",
                    containerColor = Color(0xFF0284C7),
                    onClick = onExplorePlacesClick,
                    modifier = Modifier.weight(1f),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Action 3: My Tickets
                EasyActionButton(
                    icon = Icons.Default.ConfirmationNumber,
                    labelKh = "មើលសំបុត្រខ្ញុំ",
                    labelEn = "My Tickets",
                    containerColor = Color(0xFFD97706),
                    onClick = onMyTicketsClick,
                    modifier = Modifier.weight(1f),
                )

                // Action 4: Call Help / Support
                EasyActionButton(
                    icon = Icons.Default.Call,
                    labelKh = "ទូរស័ព្ទសុំជំនួយ",
                    labelEn = "Call Support",
                    containerColor = Color(0xFFDC2626),
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0974944390"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f),
                )
            }

            // Audio Voice Guidance Banner
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Icon(
                        Icons.Default.RecordVoiceOver,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp),
                    )
                    Text(
                        if (AppLocale.isKhmer) "💡 ព័ត៌មាន៖ ចុចប៊ូតុងពណ៌បៃតងខាងលើ ដើម្បីកក់សំបុត្រ ឬចុចប៊ូតុងក្រហមដើម្បីទូរស័ព្ទសួរព័ត៌មាន" else "💡 Tip: Tap the green button above to book tickets or red button to call for help",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Composable
private fun EasyActionButton(
    icon: ImageVector,
    labelKh: String,
    labelEn: String,
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(14.dp),
        shadowElevation = 2.dp,
        modifier = modifier
            .height(72.dp)
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                if (AppLocale.isKhmer) labelKh else labelEn,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
