package com.example.e_shop.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_shop.data.model.LoginRequest
import com.example.e_shop.data.model.UserRequest
import com.example.e_shop.data.repository.AuthRepository
import com.example.e_shop.util.SafeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val registerSuccess: Boolean = false,
    val logoutSuccess: Boolean = false,
    val uploadSuccessUrl: String? = null,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenManager: com.example.e_shop.data.local.TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            repository.login(LoginRequest(email, pass)).collect { result ->
                when (result) {
                    is SafeResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    is SafeResult.Success -> _uiState.value = _uiState.value.copy(isLoading = false, loginSuccess = true)
                    is SafeResult.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearToken()
            _uiState.value = AuthUiState(logoutSuccess = true)
        }
    }

    fun uploadAvatar(file: MultipartBody.Part) {
        viewModelScope.launch {
            repository.uploadImage(file).collect { result ->
                when (result) {
                    is SafeResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    is SafeResult.Success -> _uiState.value = _uiState.value.copy(isLoading = false, uploadSuccessUrl = result.data)
                    is SafeResult.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun register(name: String, email: String, pass: String, avatar: String) {
        viewModelScope.launch {
            val finalAvatar = avatar.ifBlank { "https://i.imgur.com/LDOO4Qs.jpg" }
            repository.register(UserRequest(name, email, pass, finalAvatar)).collect { result ->
                when (result) {
                    is SafeResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    is SafeResult.Success -> _uiState.value = _uiState.value.copy(isLoading = false, registerSuccess = true)
                    is SafeResult.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun resetState() {
        _uiState.value = AuthUiState()
    }
}