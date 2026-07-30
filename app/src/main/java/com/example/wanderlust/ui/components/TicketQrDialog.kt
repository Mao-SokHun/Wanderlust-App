package com.example.wanderlust.ui.components

import com.example.wanderlust.R

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderlust.data.model.BookingTicket
import com.example.wanderlust.locale.AppLocale
import com.example.wanderlust.locale.stringApp
import com.example.wanderlust.locale.stringLocalized
import com.example.wanderlust.util.CurrencyUtils

@Composable
fun TicketQrDialog(
    ticket: BookingTicket,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Header Ticket Icon
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Default.ConfirmationNumber,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp),
                    )
                }

                Text(
                    ticket.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                Text(
                    "REF: ${ticket.bookingRef}",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                )

                // Ticket Meta Specs Grid
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Travel Date / ថ្ងៃធ្វើដំណើរ:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${ticket.travelDate} (${ticket.departureTime})", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Seat / Assignment:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(ticket.seatNumber, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Passenger:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(ticket.passengerName, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Paid:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(CurrencyUtils.formatDualPrice(ticket.priceUsd), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                // Visual Canvas QR Pattern Generator
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .border(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    SimulatedQrCanvas(modifier = Modifier.fillMaxSize())
                }

                Text(
                    if (AppLocale.isKhmer) {
                        "បង្ហាញ QR នេះដល់អ្នកបើកបរ ឬបុគ្គលិកនៅស្ថានីយដើម្បីស្កែនឡើងរថយន្ត"
                    } else {
                        "Show this QR code to the driver or terminal staff for boarding"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        },
        confirmButton = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                android.content.Intent.EXTRA_TEXT,
                                "🎟️ Wanderlust Ticket: ${ticket.title}\nRef: ${ticket.bookingRef}\nTravel Date: ${ticket.travelDate} (${ticket.departureTime})\nSeat: ${ticket.seatNumber}\nPassenger: ${ticket.passengerName}\nTotal: ${CurrencyUtils.formatDualPrice(ticket.priceUsd)}",
                            )
                        }
                        context.startActivity(android.content.Intent.createChooser(shareIntent, "Save / Share Ticket"))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp).padding(end = 6.dp),
                    )
                    Text(if (AppLocale.isKhmer) "ទាញយក / ចែករំលែកសំបុត្រ" else "Download / Share Ticket")
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Close / បិទ")
                }
            }
        },
    )
}

@Composable
private fun SimulatedQrCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val sizePx = size.width
        val cols = 7
        val cellSize = sizePx / cols

        // Draw position square markers (top-left, top-right, bottom-left)
        drawRect(Color.Black, topLeft = Offset(0f, 0f), size = Size(cellSize * 2, cellSize * 2))
        drawRect(Color.White, topLeft = Offset(cellSize * 0.3f, cellSize * 0.3f), size = Size(cellSize * 1.4f, cellSize * 1.4f))
        drawRect(Color.Black, topLeft = Offset(cellSize * 0.6f, cellSize * 0.6f), size = Size(cellSize * 0.8f, cellSize * 0.8f))

        drawRect(Color.Black, topLeft = Offset(sizePx - cellSize * 2, 0f), size = Size(cellSize * 2, cellSize * 2))
        drawRect(Color.White, topLeft = Offset(sizePx - cellSize * 1.7f, cellSize * 0.3f), size = Size(cellSize * 1.4f, cellSize * 1.4f))
        drawRect(Color.Black, topLeft = Offset(sizePx - cellSize * 1.4f, cellSize * 0.6f), size = Size(cellSize * 0.8f, cellSize * 0.8f))

        drawRect(Color.Black, topLeft = Offset(0f, sizePx - cellSize * 2), size = Size(cellSize * 2, cellSize * 2))

        // Random data modules
        for (r in 0 until cols) {
            for (c in 0 until cols) {
                if ((r < 2 && c < 2) || (r < 2 && c >= cols - 2) || (r >= cols - 2 && c < 2)) continue
                if ((r + c) % 2 == 0) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(c * cellSize + 2f, r * cellSize + 2f),
                        size = Size(cellSize - 4f, cellSize - 4f),
                    )
                }
            }
        }
    }
}
