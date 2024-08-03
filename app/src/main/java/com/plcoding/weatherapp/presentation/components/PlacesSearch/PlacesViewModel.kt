package com.plcoding.weatherapp.presentation.components.PlacesSearch

import Place
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlacesViewModel() : ViewModel() {
    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places

    fun searchPlaces(query: String) {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.searchPlaces(query, apiKey = "80cf8404123635fff2c086149b1cfbeb")
                _places.value = result
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
