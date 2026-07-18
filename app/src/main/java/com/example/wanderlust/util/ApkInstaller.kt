package com.example.wanderlust.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Downloads a release APK into app cache and launches the system installer.
 * Avoids Chrome/Play Protect "download finished but App not installed" when the
 * browser truncates the file or cannot hand off install.
 */
object ApkInstaller {
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    data class Progress(val bytesRead: Long, val totalBytes: Long?)

    /**
     * Download [url] then open the package installer.
     * @return null on success, or a short user-facing error message.
     */
    suspend fun downloadAndInstall(
        context: Context,
        url: String,
        onProgress: ((Progress) -> Unit)? = null,
    ): String? = withContext(Dispatchers.IO) {
        if (url.isBlank()) return@withContext "Missing download URL"
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                !context.packageManager.canRequestPackageInstalls()
            ) {
                // Ask user to allow installs from this app, then they can retry.
                withContext(Dispatchers.Main) {
                    openUnknownSourcesSettings(context)
                }
                return@withContext "Allow install from Wanderlust in Settings, then tap Install again."
            }

            val dir = File(context.cacheDir, "updates").apply { mkdirs() }
            val outFile = File(dir, "wanderlust-update.apk")
            if (outFile.exists()) outFile.delete()

            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "Wanderlust-Android-Updater")
                .get()
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext "Download failed (HTTP ${response.code})"
                }
                val body = response.body ?: return@withContext "Download failed (empty body)"
                val expected = body.contentLength().takeIf { it > 0 }
                body.byteStream().use { input ->
                    outFile.outputStream().use { output ->
                        val buf = ByteArray(64 * 1024)
                        var readTotal = 0L
                        while (true) {
                            val n = input.read(buf)
                            if (n <= 0) break
                            output.write(buf, 0, n)
                            readTotal += n
                            onProgress?.invoke(Progress(readTotal, expected))
                        }
                        output.flush()
                    }
                }
                // Reject truncated downloads (common after Play Protect / flaky proxies).
                if (expected != null && outFile.length() != expected) {
                    outFile.delete()
                    return@withContext "Download incomplete. Try again on Wi‑Fi."
                }
                if (outFile.length() < 1_000_000L) {
                    outFile.delete()
                    return@withContext "APK file looks corrupt. Try again."
                }
            }

            withContext(Dispatchers.Main) {
                promptInstall(context, outFile)
            }
            null
        } catch (e: Exception) {
            e.message?.takeIf { it.isNotBlank() } ?: "Could not download update"
        }
    }

    /** Opens the system APK installer for a local file via FileProvider. */
    fun promptInstall(context: Context, apkFile: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            apkFile,
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun openUnknownSourcesSettings(context: Context) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(
                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:${context.packageName}"),
            )
        } else {
            Intent(Settings.ACTION_SECURITY_SETTINGS)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching { context.startActivity(intent) }
    }

    /** Opens the public download page (Play Protect steps) in the browser. */
    fun openDownloadPage(context: Context, pageUrl: String) {
        if (pageUrl.isBlank()) return
        runCatching {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(pageUrl)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            )
        }
    }
}
