package com.example.wanderlust.ui.screens.chat

import com.example.wanderlust.R

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.filled.Flag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.wanderlust.data.SessionManager
import com.example.wanderlust.data.repository.SupportRepository
import com.example.wanderlust.data.model.ChatMessage
import com.example.wanderlust.data.model.ListingInquiryContext
import com.example.wanderlust.data.model.QuickInquiryPresets
import com.example.wanderlust.locale.AppLocale
import com.example.wanderlust.locale.stringApp
import com.example.wanderlust.locale.stringLocalized
import com.example.wanderlust.ui.components.ProfileAvatar
import com.example.wanderlust.ui.components.StitchGhostCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectChatScreen(
    partnerId: String,
    hostName: String,
    hostTelegram: String? = null,
    inquiryContext: ListingInquiryContext? = null,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val supportRepo = remember { SupportRepository() }
    val chatRepo = remember { com.example.wanderlust.data.repository.ChatRepository() }

    var inputText by remember { mutableStateOf("") }
    var showReportDialog by remember { mutableStateOf(false) }
    var reportStatus by remember { mutableStateOf<String?>(null) }
    
    val messages = remember { mutableStateListOf<ChatMessage>() }

    LaunchedEffect(partnerId) {
        if (partnerId.isBlank()) return@LaunchedEffect
        while (true) {
            try {
                chatRepo.getChatHistory(partnerId).onSuccess { apiMessages ->
                    val newMessages = apiMessages.map { apiMsg ->
                        val isMine = apiMsg.sender_id.toString() == SessionManager.userId
                        val ts = try {
                            java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US)
                                .apply { timeZone = java.util.TimeZone.getTimeZone("UTC") }
                                .parse(apiMsg.created_at)?.time ?: System.currentTimeMillis()
                        } catch (e: Exception) { System.currentTimeMillis() }

                        ChatMessage(
                            id = apiMsg.id.toString(),
                            senderId = apiMsg.sender_id.toString(),
                            senderName = if (isMine) SessionManager.userName ?: "Traveler" else hostName,
                            senderRole = if (isMine) "USER" else "BUSINESS",
                            text = apiMsg.message,
                            timestamp = ts,
                            isFromUser = isMine
                        )
                    }
                    messages.clear()
                    messages.addAll(newMessages)
                    if (messages.isNotEmpty()) {
                        listState.scrollToItem(messages.size - 1)
                    }
                }
            } catch (_: Exception) { /* network failure — retry next cycle */ }
            delay(3000)
        }
    }

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isBlank() || partnerId.isBlank()) return
        
        // Optimistic UI
        val optimistic = ChatMessage(
            senderId = SessionManager.userId ?: "user",
            senderName = SessionManager.userName ?: "Traveler",
            senderRole = "USER",
            text = trimmed,
            timestamp = System.currentTimeMillis(),
            isFromUser = true,
        )
        messages.add(optimistic)
        inputText = ""
        scope.launch { listState.animateScrollToItem(messages.size - 1) }

        scope.launch {
            chatRepo.sendChatMessage(partnerId, trimmed)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        ProfileAvatar(size = 40.dp, displayName = hostName)
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    hostName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                                Spacer(Modifier.width(4.dp))
                                Icon(
                                    Icons.Default.Storefront,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                            Text(
                                stringLocalized(R.string.chat_subtitle, R.string.chat_subtitle_kh),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    if (!hostTelegram.isNullOrBlank()) {
                        IconButton(
                            onClick = {
                                val raw = hostTelegram.trim()
                                val url = when {
                                    raw.startsWith("http") -> raw
                                    raw.startsWith("t.me/") -> "https://$raw"
                                    raw.startsWith("@") -> "https://t.me/${raw.removePrefix("@")}"
                                    else -> "https://t.me/$raw"
                                }
                                runCatching {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                }
                            },
                        ) {
                            Text(
                                "Telegram",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                            )
                        }
                    }
                    IconButton(onClick = { showReportDialog = true }) {
                        Icon(
                            Icons.Default.Flag,
                            contentDescription = "Report User",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            ) {
                // Quick Inquiry Chips Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    QuickInquiryPresets.defaultList.forEach { preset ->
                        val chipText = if (AppLocale.isKhmer) preset.textKh else preset.textEn
                        AssistChip(
                            onClick = { sendMessage(chipText) },
                            label = { Text(chipText, fontSize = 12.sp) },
                            shape = RoundedCornerShape(12.dp),
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                                labelColor = MaterialTheme.colorScheme.onSurface,
                            ),
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                // Text Input & Send Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                stringLocalized(
                                    R.string.chat_input_hint,
                                    R.string.chat_input_hint_kh,
                                ),
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        ),
                        maxLines = 3,
                    )

                    IconButton(
                        onClick = { sendMessage(inputText) },
                        enabled = inputText.isNotBlank(),
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                if (inputText.isNotBlank()) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            ),
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
        ) {
            // Warning Banner
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringLocalized(R.string.chat_safety_warning, R.string.chat_safety_warning_kh),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Context Banner (if inquiring about a specific listing)
            inquiryContext?.let { ctx ->
                StitchGhostCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        if (ctx.imageUrl.isNotBlank()) {
                            AsyncImage(
                                model = ctx.imageUrl,
                                contentDescription = ctx.title,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                stringLocalized(
                                    R.string.chat_inquiring_about,
                                    R.string.chat_inquiring_about_kh,
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                ctx.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                            )
                            if (ctx.priceLabel.isNotBlank()) {
                                Text(
                                    ctx.priceLabel,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }

            // Messages LazyColumn
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(messages, key = { it.id }) { msg ->
                    ChatBubble(message = msg)
                }
                item { Spacer(Modifier.height(12.dp)) }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val isUser = message.isFromUser
    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (isUser) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }
    val textColor = if (isUser) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val shape = if (isUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment,
    ) {
        Column(
            modifier = Modifier.widthIn(max = 280.dp),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
        ) {
            Surface(
                color = bubbleColor,
                shape = shape,
                shadowElevation = 1.dp,
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif),
                    color = textColor,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                )
            }
            Text(
                text = message.formattedTime,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp),
            )
        }
    }

    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = {
                Text(stringLocalized(R.string.report_dialog_title, R.string.report_dialog_title_kh))
            },
            text = {
                Text(reportStatus ?: stringLocalized(R.string.report_dialog_text, R.string.report_dialog_text_kh))
            },
            confirmButton = {
                if (reportStatus == null) {
                    TextButton(onClick = {
                        reportStatus = "Sending..."
                        scope.launch {
                            val msg = "Reporting Business: $hostName. From User ID: ${SessionManager.userId}. Reason: Fraud/Scam via Chat."
                            val res = supportRepo.sendMessage("Report Chat Scam", msg)
                            if (res.isSuccess) {
                                reportStatus = if (AppLocale.isKhmer) "បានបញ្ជូនពាក្យបណ្តឹង។ យើងនឹងត្រួតពិនិត្យឆាប់ៗ។" else "Report sent. We will review it soon."
                            } else {
                                reportStatus = "Error sending report."
                            }
                        }
                    }) {
                        Text(stringLocalized(R.string.report_user, R.string.report_user_kh))
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showReportDialog = false 
                    reportStatus = null
                }) {
                    Text(if (reportStatus == null) stringApp(R.string.btn_cancel) else stringApp(R.string.btn_ok))
                }
            }
        )
    }
}
