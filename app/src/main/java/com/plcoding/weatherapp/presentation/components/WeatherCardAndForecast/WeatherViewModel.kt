package com.plcoding.weatherapp.presentation.components.WeatherCardAndForecast

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
): ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    fun loadWeatherInfo(latitude: Double? = null, longitude: Double? = null) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )

            val (lat, lon) = when {
                latitude != null && longitude != null -> {
                    // Use provided latitude and longitude
                    latitude to longitude
                }
                else -> {
                    // Use location tracker if latitude and longitude are not provided
                    locationTracker.getCurrentLocation()?.let { location ->
                        location.latitude to location.longitude
                    } ?: run {
                        // Handle the case where location cannot be retrieved
                        state = state.copy(
                            isLoading = false,
                            error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                        )
                        return@launch
                    }
                }
            }

            when(val result = repository.getWeatherData(lat, lon)) {
                is Resource.Success -> {
                    state = state.copy(
                        weatherInfo = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        weatherInfo = null,
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }
}
