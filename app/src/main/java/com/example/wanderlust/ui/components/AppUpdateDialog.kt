package com.example.wanderlust.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.unit.dp
import com.example.wanderlust.R
import com.example.wanderlust.data.repository.AppUpdateAvailability
import com.example.wanderlust.locale.stringApp
import com.example.wanderlust.util.ApkInstaller
import kotlinx.coroutines.launch

@Composable
fun AppUpdateDialog(
    update: AppUpdateAvailability,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var busy by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf<String?>(null) }
    val downloadingLabel = stringApp(R.string.update_downloading)

    AlertDialog(
        onDismissRequest = {
            if (!update.forceUpdate && !busy) onDismiss()
        },
        title = {
            Text(
                stringApp(
                    if (update.forceUpdate) R.string.update_required_title else R.string.update_available_title,
                ),
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(
                status
                    ?: stringApp(R.string.update_available_body, update.info.versionName),
            )
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (status != null && update.info.downloadPageUrl.isNotBlank()) {
                    TextButton(
                        onClick = {
                            ApkInstaller.openDownloadPage(context, update.info.downloadPageUrl)
                        }
                    ) {
                        Text(if (com.example.wanderlust.locale.AppLocale.isKhmer) "ទាញយកតាម Chrome" else "Open in Browser")
                    }
                }
                TextButton(
                    enabled = !busy,
                    onClick = {
                        scope.launch {
                            busy = true
                            status = downloadingLabel
                            val err = ApkInstaller.downloadAndInstall(
                                context = context,
                                url = update.downloadUrl,
                            )
                            busy = false
                            if (err != null) {
                                status = err
                            } else if (!update.forceUpdate) {
                                onDismiss()
                            } else {
                                status = null
                            }
                        }
                    },
                ) {
                    Text(
                        if (busy) downloadingLabel else stringApp(R.string.update_download),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        },
        dismissButton = {
            if (!update.forceUpdate && !busy) {
                TextButton(onClick = onDismiss) {
                    Text(stringApp(R.string.update_later))
                }
            }
        },
    )
}
