package com.example.nominatim.ui

import Place
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlacesViewModel(private val apiKey: String) : ViewModel() {
    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places

    fun searchPlaces(query: String) {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.searchPlaces(query, apiKey = apiKey)
                _places.value = result
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
