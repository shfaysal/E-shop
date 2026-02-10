package com.example.e_shop.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_shop.data.model.UserRequest
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

data class EditProfileUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val currentUser: UserResponse? = null,
    val uploadedAvatarUrl: String? = null
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            authRepository.getProfile().collect { result ->
                when (result) {
                    is SafeResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is SafeResult.Success -> _uiState.update { it.copy(isLoading = false, currentUser = result.data) }
                    is SafeResult.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    fun uploadImage(filePart: okhttp3.MultipartBody.Part) {
        viewModelScope.launch {
            authRepository.uploadImage(filePart).collect { result ->
                when (result) {
                    is SafeResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is SafeResult.Success -> {
                        _uiState.update { it.copy(isLoading = false, uploadedAvatarUrl = result.data) }
                    }
                    is SafeResult.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    fun updateProfile(name: String, email: String, avatar: String) {
        val userId = _uiState.value.currentUser?.id ?: return
        
        viewModelScope.launch {
            val finalAvatar = _uiState.value.uploadedAvatarUrl ?: avatar
            val request = UserRequest(name = name, email = email, avatar = finalAvatar)
            authRepository.updateProfile(userId, request).collect { result ->
                when (result) {
                    is SafeResult.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                    is SafeResult.Success -> _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    is SafeResult.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
