package com.chocomiruku.myhome.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chocomiruku.myhome.domain.models.News
import com.chocomiruku.myhome.domain.usecase.images.UploadImageUseCase
import com.chocomiruku.myhome.domain.usecase.news.AddNewsUseCase
import com.chocomiruku.myhome.domain.usecase.news.UpdateNewsUseCase
import com.chocomiruku.myhome.util.ImageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNewsViewModel @Inject constructor(
    private val addNewsUseCase: AddNewsUseCase,
    private val updateNewsUseCase: UpdateNewsUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
) : ViewModel() {
    private val _navigateUpFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val navigateUpFlow: StateFlow<Boolean> = _navigateUpFlow

    fun addNews(title: String, text: String, imageBytes: ByteArray?) {
        viewModelScope.launch {
            if (imageBytes != null) {
                uploadImageUseCase.invoke(imageBytes, TYPE).collect { imageUri ->
                    imageUri?.let {
                        addNewsUseCase.invoke(
                            News(
                                newsId = "",
                                title = title,
                                text = text,
                                date = System.currentTimeMillis(),
                                imageUri = imageUri
                            )
                        )
                    }
                    _navigateUpFlow.value = true
                }
            } else {
                addNewsUseCase.invoke(
                    News(
                        newsId = "",
                        title = title,
                        text = text,
                        date = System.currentTimeMillis()
                    )
                )
                _navigateUpFlow.value = true
            }
        }
    }

    fun updateNews(newsId: String, title: String, text: String, imageBytes: ByteArray?) {
        viewModelScope.launch {
            if (imageBytes != null) {
                uploadImageUseCase.invoke(imageBytes, TYPE).collect { imageUri ->
                    imageUri?.let {
                        updateNewsUseCase.invoke(
                            newsId = newsId,
                            title = title,
                            text = text,
                            imagePath = imageUri
                        )
                    }
                    _navigateUpFlow.value = true
                }
            } else {
                updateNewsUseCase.invoke(
                    newsId = newsId,
                    title = title,
                    text = text,
                    imagePath = null
                )
                _navigateUpFlow.value = true
            }
        }
    }

    private companion object {
        val TYPE = ImageType.NEWS
    }
}