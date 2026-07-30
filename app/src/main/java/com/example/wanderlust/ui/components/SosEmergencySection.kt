package com.example.wanderlust.ui.components

import com.example.wanderlust.R

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderlust.locale.AppLocale

@Composable
fun SosEmergencySection(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val policeNumber = "117"
    val ambulanceNumber = "119"

    fun callHotline(num: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$num"))
        context.startActivity(intent)
    }

    StitchGhostCard(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(24.dp),
                )
                Column {
                    Text(
                        if (AppLocale.isKhmer) "🚨 SOS សង្គ្រោះបន្ទាន់ 24/7" else "🚨 24/7 Traveler SOS Assistance",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626),
                    )
                    Text(
                        if (AppLocale.isKhmer) "ចុចហៅទូរស័ព្ទ ឬចែករំលែកទីតាំងពេលមានអាសន្ន" else "Tap to call hotline or share GPS location in emergency",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // Hotline Call Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = { callHotline(policeNumber) },
                    modifier = Modifier.weight(1f).height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(16.dp).padding(end = 4.dp))
                    Text(if (AppLocale.isKhmer) "ប៉ូលិស (117)" else "Police (117)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { callHotline(ambulanceNumber) },
                    modifier = Modifier.weight(1f).height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                ) {
                    Icon(Icons.Default.LocalHospital, contentDescription = null, modifier = Modifier.size(16.dp).padding(end = 4.dp))
                    Text(if (AppLocale.isKhmer) "សង្គ្រោះ (119)" else "Ambulance", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Share GPS Location
            OutlinedButton(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "🚨 SOS EMERGENCY ALERT! I need assistance.\nMy Current GPS Location: https://maps.google.com/?q=13.3671,103.8448\n(Siem Reap, Cambodia)",
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Emergency Location"))
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp).padding(end = 6.dp))
                Text(if (AppLocale.isKhmer) "ចែករំលែកទីតាំង GPS បន្ទាន់" else "Share Emergency GPS Location", fontWeight = FontWeight.Bold)
            }
        }
    }
}
