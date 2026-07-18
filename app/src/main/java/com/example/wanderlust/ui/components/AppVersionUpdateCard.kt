package com.example.wanderlust.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wanderlust.BuildConfig
import com.example.wanderlust.R
import com.example.wanderlust.data.repository.AppUpdateAvailability
import com.example.wanderlust.data.repository.AppUpdateRepository
import com.example.wanderlust.locale.stringApp
import com.example.wanderlust.util.ApkInstaller
import kotlinx.coroutines.launch

/**
 * Shows installed version, checks the API for a newer build, and offers Install when available.
 */
@Composable
fun AppVersionUpdateCard(
    cardModifier: Modifier = Modifier,
    onUpdateFound: ((AppUpdateAvailability) -> Unit)? = null,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repo = remember { AppUpdateRepository() }
    var checking by remember { mutableStateOf(true) }
    var update by remember { mutableStateOf<AppUpdateAvailability?>(null) }
    var latestInfo by remember { mutableStateOf<com.example.wanderlust.data.model.AppVersionInfo?>(null) }
    var latestLabel by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var installing by remember { mutableStateOf(false) }
    var installProgress by remember { mutableFloatStateOf(0f) }
    var installMessage by remember { mutableStateOf<String?>(null) }
    val installed = AppUpdateRepository.installedVersionLabel()
    val downloadingLabel = stringApp(R.string.update_downloading)

    fun openUri(url: String) {
        if (url.isBlank()) return
        runCatching {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    // Download APK in-app then open the system installer (avoids Chrome truncate).
    fun installUpdate(url: String) {
        if (url.isBlank() || installing) return
        scope.launch {
            installing = true
            installProgress = 0f
            installMessage = downloadingLabel
            val err = ApkInstaller.downloadAndInstall(context, url) { p ->
                val total = p.totalBytes
                installProgress = if (total != null && total > 0) {
                    (p.bytesRead.toFloat() / total.toFloat()).coerceIn(0f, 1f)
                } else {
                    0f
                }
            }
            installing = false
            installMessage = err
        }
    }

    fun refresh() {
        scope.launch {
            checking = true
            error = null
            repo.checkForUpdate()
                .onSuccess { avail ->
                    update = avail
                    if (avail != null) {
                        latestInfo = avail.info
                        latestLabel = avail.info.versionName
                        onUpdateFound?.invoke(avail)
                    } else {
                        repo.fetchLatestInfo()
                            .onSuccess { info ->
                                latestInfo = info
                                latestLabel = info.versionName
                            }
                    }
                }
                .onFailure {
                    error = it.message
                    update = null
                }
            checking = false
        }
    }

    LaunchedEffect(Unit) { refresh() }

    StitchGhostCard(cardModifier.fillMaxWidth()) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SystemUpdate,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text(
                    text = stringApp(R.string.about_version_section),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Text(
                text = stringApp(R.string.about_installed_version, installed),
                style = MaterialTheme.typography.bodyMedium,
            )

            when {
                checking -> {
                    Text(
                        text = stringApp(R.string.about_checking_update),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                update != null -> {
                    val avail = update!!
                    Text(
                        text = stringApp(
                            R.string.about_update_available,
                            avail.info.versionName,
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    if (avail.info.releaseNotes.isNotBlank()) {
                        Text(
                            text = avail.info.releaseNotes,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Button(
                        onClick = { installUpdate(avail.downloadUrl) },
                        enabled = !installing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                    ) {
                        Text(
                            text = if (installing) {
                                downloadingLabel
                            } else {
                                stringApp(R.string.about_install_update)
                            },
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    if (installing) {
                        LinearProgressIndicator(
                            progress = { installProgress },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    installMessage?.let { msg ->
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    Text(
                        text = stringApp(R.string.about_install_hint),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    OutlinedButton(
                        onClick = {
                            val page = latestInfo?.downloadPageUrl?.takeIf { it.isNotBlank() }
                                ?: "https://wanderlust-api-dm3y.onrender.com/download/"
                            openUri(page)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Text(stringApp(R.string.update_open_download_page))
                    }
                }
                error != null -> {
                    Text(
                        text = error!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                    OutlinedButton(
                        onClick = { refresh() },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = stringApp(R.string.about_check_again))
                    }
                }
                else -> {
                    latestLabel?.let { latest ->
                        Text(
                            text = stringApp(R.string.about_latest_version, latest),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Text(
                        text = stringApp(R.string.about_up_to_date),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    OutlinedButton(
                        onClick = { refresh() },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = stringApp(R.string.about_check_again))
                    }
                }
            }

            Text(
                text = "Build ${BuildConfig.VERSION_NAME} · code ${BuildConfig.VERSION_CODE}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
            )

            val iosPage = latestInfo?.iosDownloadPageUrl?.takeIf { it.isNotBlank() }
                ?: latestInfo?.downloadPageUrl?.let { base ->
                    val trimmed = base.trimEnd('/')
                    if (trimmed.contains("platform=ios")) trimmed else "$trimmed/?platform=ios"
                }
            val iosDirect = latestInfo?.iosDownloadUrl?.takeIf { it.isNotBlank() }
            OutlinedButton(
                onClick = {
                    openUri(iosDirect ?: iosPage.orEmpty().ifBlank {
                        "https://wanderlust-api-dm3y.onrender.com/download/?platform=ios"
                    })
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
            ) {
                Text(
                    text = if (latestInfo?.iosAvailable == true) {
                        stringApp(R.string.about_install_iphone)
                    } else {
                        stringApp(R.string.about_iphone_guide)
                    },
                )
            }
            Text(
                text = stringApp(
                    R.string.about_iphone_hint,
                    latestInfo?.iosMinVersion?.takeIf { it.isNotBlank() } ?: "15.0",
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
