package com.example.e_shop.ui.profile

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_shop.data.local.SettingsRepository
import com.example.e_shop.data.local.TokenManager
import com.example.e_shop.data.model.UserResponse
import com.example.e_shop.data.repository.AuthRepository
import com.example.e_shop.util.SafeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: UserResponse? = null,
    val error: String? = null,
    val isDarkMode: Boolean = false,
    val language: String = "en",
    val isLogoutSuccess: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        fetchProfile()
        observeTheme()
        // Initialize language from current app locale
        val currentLang = AppCompatDelegate.getApplicationLocales().toLanguageTags().split("-").firstOrNull() ?: "en"
        _uiState.update { it.copy(language = if (currentLang.isEmpty()) "en" else currentLang) }
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            authRepository.getProfile().collect { result ->
                when (result) {
                    is SafeResult.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                    is SafeResult.Success -> _uiState.update { it.copy(isLoading = false, user = result.data) }
                    is SafeResult.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    private fun observeTheme() {
        viewModelScope.launch {
            settingsRepository.isDarkMode.collect { isDark ->
                _uiState.update { it.copy(isDarkMode = isDark ?: false) } // Default to system/false if null
            }
        }
    }

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(isDark)
        }
    }
    
    fun setLanguage(language: String) {
        val appLocale = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearToken()
            _uiState.update { it.copy(isLogoutSuccess = true) }
        }
    }
    
    fun resetLogoutState() {
        _uiState.update { it.copy(isLogoutSuccess = false) }
    }
}