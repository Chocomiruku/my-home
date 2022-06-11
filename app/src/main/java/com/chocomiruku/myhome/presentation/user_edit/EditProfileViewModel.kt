package com.chocomiruku.myhome.presentation.user_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chocomiruku.myhome.domain.usecase.images.UploadImageUseCase
import com.chocomiruku.myhome.domain.usecase.user.UpdateCurrentUserUseCase
import com.chocomiruku.myhome.util.ImageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    private val uploadImageUseCase: UploadImageUseCase
) : ViewModel() {
    private val _navigateUpFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val navigateUpFlow: StateFlow<Boolean> = _navigateUpFlow

    fun updateUserDetails(
        name: String,
        email: String,
        imageBytes: ByteArray?,
        notifications: Boolean
    ) {
        viewModelScope.launch {
            if (imageBytes != null) {
                uploadImageUseCase.invoke(imageBytes, TYPE).collect { imageUri ->
                    imageUri?.let {
                        updateCurrentUserUseCase.invoke(name, email, imageUri, notifications)
                    }
                    _navigateUpFlow.value = true
                }
            } else {
                updateCurrentUserUseCase.invoke(name, email, null, notifications)
                _navigateUpFlow.value = true
            }
        }
    }

    private companion object {
        val TYPE = ImageType.USER
    }
}