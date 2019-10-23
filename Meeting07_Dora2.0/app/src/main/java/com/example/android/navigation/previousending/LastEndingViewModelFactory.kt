package com.example.android.navigation.previousending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class LastEndingViewModelFactory (private val previousEnd: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LastEndingViewModel::class.java)) {
            return LastEndingViewModel(previousEnd) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}