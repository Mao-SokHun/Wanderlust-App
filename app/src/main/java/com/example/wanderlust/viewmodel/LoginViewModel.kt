package com.example.wanderlust.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlust.data.repository.AuthRepository
import com.example.wanderlust.util.Validation
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
)

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository(),
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value.take(Validation.EMAIL_MAX), errorMessage = null, emailError = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value.take(Validation.PASSWORD_MAX), errorMessage = null, passwordError = null)
    }

    fun login() {
        val email = uiState.email.trim()
        val password = uiState.password
        // Field-level validation — highlight the exact failing field
        val emailErr = Validation.requireEmail(Validation.normalizeEmail(email))
        val passErr = if (emailErr == null) Validation.requirePassword(password) else null
        if (emailErr != null || passErr != null) {
            uiState = uiState.copy(emailError = emailErr, passwordError = passErr)
            return
        }
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null, emailError = null, passwordError = null, loginSuccess = false)
            repository.login(Validation.normalizeEmail(email), password)
                .onSuccess {
                    uiState = uiState.copy(isLoading = false, loginSuccess = true)
                }
                .onFailure { error ->
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Invalid email or password. Please try again.",
                    )
                }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null, loginSuccess = false)
            repository.socialLogin(provider = "google", idToken = idToken)
                .onSuccess {
                    uiState = uiState.copy(isLoading = false, loginSuccess = true)
                }
                .onFailure { error ->
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Google sign-in failed",
                    )
                }
        }
    }

    fun loginWithFacebook(accessToken: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null, loginSuccess = false)
            repository.socialLogin(provider = "facebook", accessToken = accessToken)
                .onSuccess {
                    uiState = uiState.copy(isLoading = false, loginSuccess = true)
                }
                .onFailure { error ->
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Facebook sign-in failed",
                    )
                }
        }
    }

    fun setError(message: String) {
        uiState = uiState.copy(errorMessage = message, isLoading = false)
    }

    fun resetSuccess() {
        uiState = uiState.copy(loginSuccess = false)
    }
}
