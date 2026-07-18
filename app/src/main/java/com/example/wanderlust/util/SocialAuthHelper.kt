package com.example.wanderlust.util

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.wanderlust.BuildConfig
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resume

sealed class SocialAuthResult {
    data class Google(val idToken: String) : SocialAuthResult()
    data class Facebook(val accessToken: String) : SocialAuthResult()
    data class Error(val message: String) : SocialAuthResult()
}

object SocialAuthHelper {
    val facebookCallbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }

    fun googleConfigured(): Boolean = BuildConfig.GOOGLE_WEB_CLIENT_ID.isNotBlank()

    fun facebookConfigured(): Boolean =
        BuildConfig.FACEBOOK_APP_ID.isNotBlank() &&
            BuildConfig.FACEBOOK_APP_ID != "0" &&
            BuildConfig.FACEBOOK_CLIENT_TOKEN.isNotBlank() &&
            BuildConfig.FACEBOOK_CLIENT_TOKEN != "0"

    /**
     * Google login/register via Credential Manager.
     * Tries One Tap first, then full "Sign in with Google" (fixes "No credentials available").
     */
    suspend fun signInWithGoogle(context: Context): SocialAuthResult {
        val clientId = BuildConfig.GOOGLE_WEB_CLIENT_ID.trim()
        if (clientId.isEmpty()) {
            return SocialAuthResult.Error(
                "Google sign-in is not configured on this build.",
            )
        }
        // Credential UI needs an Activity; fall back to context if somehow missing.
        val uiContext = (context as? Activity) ?: context
        val manager = CredentialManager.create(uiContext)

        return try {
            // Button tap → full Sign in with Google (account picker). More reliable than One Tap.
            runSignInWithGoogleRequest(manager, uiContext, clientId)
        } catch (e: GetCredentialCancellationException) {
            SocialAuthResult.Error("Google sign-in cancelled")
        } catch (e: NoCredentialException) {
            try {
                runGoogleIdRequest(
                    manager = manager,
                    context = uiContext,
                    clientId = clientId,
                    filterAuthorizedOnly = false,
                )
            } catch (e2: GetCredentialException) {
                SocialAuthResult.Error(friendlyGoogleError(e2))
            } catch (e2: Exception) {
                SocialAuthResult.Error(e2.message ?: "Google sign-in failed")
            }
        } catch (e: GetCredentialException) {
            if (isNoCredentialsMessage(e.message)) {
                try {
                    runGoogleIdRequest(
                        manager = manager,
                        context = uiContext,
                        clientId = clientId,
                        filterAuthorizedOnly = false,
                    )
                } catch (e2: GetCredentialException) {
                    SocialAuthResult.Error(friendlyGoogleError(e2))
                } catch (e2: Exception) {
                    SocialAuthResult.Error(e2.message ?: "Google sign-in failed")
                }
            } else {
                SocialAuthResult.Error(friendlyGoogleError(e))
            }
        } catch (e: Exception) {
            SocialAuthResult.Error(e.message ?: "Google sign-in failed")
        }
    }

    private suspend fun runGoogleIdRequest(
        manager: CredentialManager,
        context: Context,
        clientId: String,
        filterAuthorizedOnly: Boolean,
    ): SocialAuthResult {
        val option = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filterAuthorizedOnly)
            .setServerClientId(clientId)
            .setAutoSelectEnabled(false)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()
        val response = manager.getCredential(request = request, context = context)
        return parseGoogleCredential(response.credential)
    }

    /** Full account-picker Sign in with Google (recommended for the Google button). */
    private suspend fun runSignInWithGoogleRequest(
        manager: CredentialManager,
        context: Context,
        clientId: String,
    ): SocialAuthResult {
        val option = GetSignInWithGoogleOption.Builder(clientId)
            .setNonce(UUID.randomUUID().toString())
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()
        val response = manager.getCredential(request = request, context = context)
        return parseGoogleCredential(response.credential)
    }

    private fun parseGoogleCredential(credential: androidx.credentials.Credential): SocialAuthResult {
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val google = GoogleIdTokenCredential.createFrom(credential.data)
            val token = google.idToken
            return if (token.isNullOrBlank()) {
                SocialAuthResult.Error("Google did not return an ID token.")
            } else {
                SocialAuthResult.Google(token)
            }
        }
        return SocialAuthResult.Error("Unexpected Google credential type.")
    }

    private fun isNoCredentialsMessage(message: String?): Boolean {
        val m = message?.lowercase().orEmpty()
        return m.contains("no credentials") || m.contains("cannot find a matching credential")
    }

    /** Maps raw Credential Manager errors to short user-facing text. */
    private fun friendlyGoogleError(e: GetCredentialException): String {
        val raw = e.message.orEmpty()
        return when {
            e is GetCredentialCancellationException ||
                raw.contains("cancel", ignoreCase = true) ->
                "Google sign-in cancelled"
            isNoCredentialsMessage(raw) ->
                "No Google account found. Add a Google account in phone Settings, then try again."
            raw.contains("network", ignoreCase = true) ->
                "Network error during Google sign-in. Check internet and try again."
            else -> raw.ifBlank { "Google sign-in failed" }
        }
    }

    /** Maps Facebook SDK errors to short user-facing text. */
    private fun friendlyFacebookError(error: FacebookException): String {
        val raw = error.message.orEmpty()
        return when {
            raw.contains("INVALID_APP_ID", ignoreCase = true) ||
                raw.contains("invalid app id", ignoreCase = true) ||
                raw.contains("Content not found", ignoreCase = true) ->
                "Facebook App ID is invalid or deleted. Create a new app in Meta Developer Console."
            raw.contains("CONNECTION_FAILURE", ignoreCase = true) ||
                raw.contains("net", ignoreCase = true) ->
                "Network error during Facebook sign-in. Check internet and try again."
            raw.contains("User canceled", ignoreCase = true) ||
                raw.contains("cancel", ignoreCase = true) ->
                "Facebook sign-in cancelled"
            else -> raw.ifBlank { "Facebook sign-in failed" }
        }
    }

    suspend fun signInWithFacebook(activity: Activity): SocialAuthResult {
        if (!facebookConfigured()) {
            return SocialAuthResult.Error(
                "Facebook sign-in is not configured on this build.",
            )
        }
        val existing = AccessToken.getCurrentAccessToken()
        if (existing != null && !existing.isExpired) {
            return SocialAuthResult.Facebook(existing.token)
        }
        return suspendCancellableCoroutine { cont ->
            val loginManager = LoginManager.getInstance()
            loginManager.logOut()
            loginManager.registerCallback(
                facebookCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        loginManager.unregisterCallback(facebookCallbackManager)
                        val token = result.accessToken?.token
                        if (token.isNullOrBlank()) {
                            cont.resume(SocialAuthResult.Error("Facebook did not return a token."))
                        } else {
                            cont.resume(SocialAuthResult.Facebook(token))
                        }
                    }

                    override fun onCancel() {
                        loginManager.unregisterCallback(facebookCallbackManager)
                        cont.resume(SocialAuthResult.Error("Facebook sign-in cancelled"))
                    }

                    override fun onError(error: FacebookException) {
                        loginManager.unregisterCallback(facebookCallbackManager)
                        cont.resume(SocialAuthResult.Error(friendlyFacebookError(error)))
                    }
                },
            )
            cont.invokeOnCancellation {
                loginManager.unregisterCallback(facebookCallbackManager)
            }
            // Prefer native/FB app, fall back to browser Custom Tab.
            loginManager.setLoginBehavior(com.facebook.login.LoginBehavior.NATIVE_WITH_FALLBACK)
            loginManager.logInWithReadPermissions(activity, listOf("email", "public_profile"))
        }
    }
}
