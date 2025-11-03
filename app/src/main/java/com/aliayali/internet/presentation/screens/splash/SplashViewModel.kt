package com.aliayali.internet.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {
    private val _isDelayFinished = MutableStateFlow(false)
    val isDelayFinished: StateFlow<Boolean> = _isDelayFinished

    init {
        viewModelScope.launch {
            delay(3000)
            _isDelayFinished.value = true
        }
    }
}