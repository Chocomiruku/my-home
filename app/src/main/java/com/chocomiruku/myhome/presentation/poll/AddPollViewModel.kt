package com.chocomiruku.myhome.presentation.poll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chocomiruku.myhome.domain.models.Poll
import com.chocomiruku.myhome.domain.usecase.poll.AddPolUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPollViewModel @Inject constructor(
    private val addPolUseCase: AddPolUseCase
) : ViewModel() {
    private val _navigateUpFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val navigateUpFlow: StateFlow<Boolean> = _navigateUpFlow

    fun addPoll(title: String, multipleChoices: Boolean, vararg selectedOptions: String?) {
        val optionsMap = mutableMapOf<String, Int>()
        selectedOptions.filterNot { option ->
            option.isNullOrBlank()
        }.forEach { option ->
            optionsMap[option!!] = 0
        }

        viewModelScope.launch {
            addPolUseCase.invoke(
                Poll(
                    pollId = "",
                    title = title,
                    options = optionsMap,
                    date = System.currentTimeMillis(),
                    multipleChoice = multipleChoices,
                    closed = false,
                    voted = mutableListOf()
                )
            )
            _navigateUpFlow.value = true
        }
    }
}