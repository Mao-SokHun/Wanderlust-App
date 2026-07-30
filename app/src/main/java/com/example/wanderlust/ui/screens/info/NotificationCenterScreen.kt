package com.example.wanderlust.ui.screens.info

import com.example.wanderlust.R

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wanderlust.data.model.AppNotification
import com.example.wanderlust.data.model.NotificationType
import com.example.wanderlust.data.model.SampleNotifications
import com.example.wanderlust.locale.AppLocale
import com.example.wanderlust.locale.stringApp
import com.example.wanderlust.ui.components.StitchGhostCard
import com.example.wanderlust.ui.components.StickyScrollScreen

@Composable
fun NotificationCenterScreen(
    onBack: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val notifications = remember {
        mutableStateListOf<AppNotification>().apply {
            addAll(SampleNotifications.sampleList)
        }
    }

    val unreadCount = notifications.count { !it.read }

    val filteredList = when (selectedTab) {
        1 -> notifications.filter { it.type == NotificationType.BOOKING }
        2 -> notifications.filter { it.type == NotificationType.CHAT }
        3 -> notifications.filter { it.type == NotificationType.SYSTEM }
        else -> notifications
    }

    fun markAllRead() {
        for (i in notifications.indices) {
            notifications[i] = notifications[i].copy(read = true)
        }
    }

    StickyScrollScreen(
        title = if (AppLocale.isKhmer) "ការជូនដំណឹង" else "Notifications",
        onBack = onBack,
    ) {
        // Header Bar: Unread Count & Mark All Read
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (unreadCount > 0) {
                    Text(
                        if (AppLocale.isKhmer) "$unreadCount មិនទាន់អាន" else "$unreadCount Unread",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
            }

            if (unreadCount > 0) {
                TextButton(onClick = { markAllRead() }) {
                    Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                    Text(
                        if (AppLocale.isKhmer) "អានទាំងអស់" else "Mark all read",
                        modifier = Modifier.padding(start = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }

        // Category Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .padding(bottom = 14.dp),
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text(if (AppLocale.isKhmer) "ទាំងអស់" else "All") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text(if (AppLocale.isKhmer) "ការកក់" else "Bookings") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text(if (AppLocale.isKhmer) "សារឆាត" else "Chat") })
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text(if (AppLocale.isKhmer) "ប្រព័ន្ធ" else "System") })
        }

        if (filteredList.isEmpty()) {
            StitchGhostCard(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        Icons.Default.NotificationsNone,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    )
                    Text(
                        if (AppLocale.isKhmer) "គ្មានការជូនដំណឹងទេ" else "No notifications",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                filteredList.forEach { notif ->
                    NotificationItemCard(
                        notification = notif,
                        onClick = {
                            val idx = notifications.indexOfFirst { it.id == notif.id }
                            if (idx != -1) {
                                notifications[idx] = notif.copy(read = true)
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationItemCard(
    notification: AppNotification,
    onClick: () -> Unit,
) {
    val title = if (AppLocale.isKhmer) notification.titleKh else notification.titleEn
    val message = if (AppLocale.isKhmer) notification.messageKh else notification.messageEn

    val icon = when (notification.type) {
        NotificationType.BOOKING -> Icons.Default.ConfirmationNumber
        NotificationType.CHAT -> Icons.Default.Chat
        NotificationType.SYSTEM -> Icons.Default.Notifications
    }

    StitchGhostCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (!notification.read) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (!notification.read) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp),
                )
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (!notification.read) FontWeight.Bold else FontWeight.SemiBold,
                    )
                    if (!notification.read) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                        )
                    }
                }
                Text(
                    message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    notification.formattedTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
    }
}
