package com.chocomiruku.myhome.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.News
import com.chocomiruku.myhome.domain.models.User
import com.chocomiruku.myhome.domain.usecase.news.DeleteNewsUseCase
import com.chocomiruku.myhome.domain.usecase.news.GetNewsUseCase
import com.chocomiruku.myhome.domain.usecase.user.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val deleteNewsUseCase: DeleteNewsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _userDetailsStateFlow: MutableStateFlow<Resource<User>> =
        MutableStateFlow(Resource.Loading)
    val userDetailsStateFlow: StateFlow<Resource<User>> = _userDetailsStateFlow

    private val _newsStateFlow: MutableStateFlow<Resource<List<News>>> =
        MutableStateFlow(Resource.Loading)
    val newsStateFlow: StateFlow<Resource<List<News>>> = _newsStateFlow

    fun getUserDetails() {
        viewModelScope.launch {
            getCurrentUserUseCase.invoke().collect { resource ->
                _userDetailsStateFlow.value = resource
            }
        }
    }

    fun getNews() {
        viewModelScope.launch {
            getNewsUseCase.invoke().collect { resource ->
                _newsStateFlow.value = resource
            }
        }
    }

    fun deleteNews(newsId: String) {
        viewModelScope.launch {
            deleteNewsUseCase.invoke(newsId)
        }
    }
}