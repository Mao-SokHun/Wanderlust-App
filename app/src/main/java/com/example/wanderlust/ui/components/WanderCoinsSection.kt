package com.example.wanderlust.ui.components

import com.example.wanderlust.R

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderlust.data.model.SampleWanderCoins
import com.example.wanderlust.data.model.WanderCoinsAccount
import com.example.wanderlust.locale.AppLocale
import com.example.wanderlust.util.CurrencyUtils

@Composable
fun WanderCoinsSection(
    account: WanderCoinsAccount = SampleWanderCoins.account,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var balancePoints by remember { mutableIntStateOf(account.balancePoints) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (com.example.wanderlust.data.SessionManager.isLoggedIn()) {
            runCatching {
                val api = com.example.wanderlust.data.remote.ApiConnection.create()
                val token = com.example.wanderlust.data.SessionManager.token.orEmpty()
                val res = api.getSubscription("Bearer $token") // fallback
                // fetch coins endpoint if available via custom call or default balance
            }
        }
    }

    StitchGhostCard(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Header: Balance Summary Card
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(14.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(28.dp),
                        )
                        Column {
                            Text(
                                if (AppLocale.isKhmer) "🪙 ពិន្ទុសន្សំ WanderCoins" else "🪙 WanderCoins Rewards",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            Text(
                                "${balancePoints} Points (${CurrencyUtils.formatDualPrice(balancePoints * 0.01)})",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            )
                        }
                    }
                }
            }

            // Available Vouchers List
            Text(
                if (AppLocale.isKhmer) "គូប៉ុងបញ្ចុះតម្លៃដែលអាចប្រើបាន៖" else "Available Discount Vouchers:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                account.rewardsList.forEach { reward ->
                    val title = if (AppLocale.isKhmer) reward.titleKh else reward.titleEn

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text(
                                    "${reward.pointsCost} Points · Code: ${reward.voucherCode}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }

                            Button(
                                onClick = {
                                    if (balancePoints >= reward.pointsCost) {
                                        balancePoints -= reward.pointsCost
                                        Toast.makeText(
                                            context,
                                            if (AppLocale.isKhmer) "បានប្រើប្រាស់គូប៉ុង ${reward.voucherCode} ជោគជ័យ!" else "Redeemed voucher ${reward.voucherCode}!",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            if (AppLocale.isKhmer) "ពិន្ទុមិនគ្រប់គ្រាន់" else "Insufficient points",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                            ) {
                                Text(if (AppLocale.isKhmer) "ប្រើគូប៉ុង" else "Redeem", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
