package com.example.wanderlust.ui.components

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wanderlust.R
import com.example.wanderlust.data.remote.ApiConnection
import com.example.wanderlust.data.repository.AppUpdateRepository
import com.example.wanderlust.locale.stringApp
import com.example.wanderlust.util.KhqrBitmap

private const val FALLBACK_DOWNLOAD_PAGE =
    "https://wanderlust-api-dm3y.onrender.com/download/"

/**
 * Invite friends with a QR that always points at the live download page.
 * URL is loaded from `/api/app/version` so new releases keep the same QR target.
 */
@Composable
fun ShareInviteQrDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val repo = remember { AppUpdateRepository() }

    var pageUrl by remember { mutableStateOf(FALLBACK_DOWNLOAD_PAGE) }
    var versionLabel by remember { mutableStateOf<String?>(null) }
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var status by remember { mutableStateOf<String?>(null) }
    val copiedLabel = stringApp(R.string.about_share_qr_copied)
    val shareTitle = stringApp(R.string.about_share_qr_title)
    val shareBodyTemplate = stringApp(R.string.about_share_qr_share_body, "<<URL>>")

    // Resolve stable download URL from API (falls back to known Render page).
    LaunchedEffect(Unit) {
        val base = ApiConnection.activeUrl()?.trimEnd('/')
        pageUrl = if (!base.isNullOrBlank()) "$base/download/" else FALLBACK_DOWNLOAD_PAGE
        repo.fetchLatestInfo()
            .onSuccess { info ->
                val fromApi = info.downloadPageUrl.trim()
                if (fromApi.isNotBlank()) {
                    pageUrl = fromApi
                }
                if (info.versionName.isNotBlank()) {
                    versionLabel = info.versionName
                }
            }
        qrBitmap = KhqrBitmap.encode(pageUrl, sizePx = 720)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                shareTitle,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    stringApp(R.string.about_share_qr_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                versionLabel?.let { ver ->
                    Text(
                        stringApp(R.string.about_share_qr_latest, ver),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Spacer(Modifier.height(4.dp))
                val bmp = qrBitmap
                if (bmp != null) {
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = shareTitle,
                        modifier = Modifier
                            .size(220.dp)
                            .padding(4.dp),
                    )
                } else {
                    Text(
                        stringApp(R.string.about_share_qr_loading),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    stringApp(R.string.about_share_qr_protect),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                status?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(
                        onClick = {
                            clipboard.setText(AnnotatedString(pageUrl))
                            status = copiedLabel
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text(stringApp(R.string.about_share_qr_copy))
                    }
                    OutlinedButton(
                        onClick = {
                            val shareText = shareBodyTemplate.replace("<<URL>>", pageUrl)
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(
                                Intent.createChooser(intent, shareTitle),
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text(stringApp(R.string.about_share_qr_share))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringApp(R.string.about_share_qr_close))
            }
        },
    )
}
