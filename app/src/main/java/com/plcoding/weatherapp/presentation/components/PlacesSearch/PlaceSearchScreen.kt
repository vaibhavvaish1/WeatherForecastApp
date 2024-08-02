package com.plcoding.weatherapp.presentation.components.PlacesSearch

import Place
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plcoding.weatherapp.presentation.components.WeatherCardAndForecast.WeatherViewModel
import com.plcoding.weatherapp.presentation.ui.theme.DarkBlue

@Composable
fun PlaceSearchScreen(viewModel: PlacesViewModel = viewModel(), weatherViewModel: WeatherViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    var isLazyColumnVisible by remember { mutableStateOf(false) }

    LaunchedEffect(query) {
        if (query.length > 2) {
            viewModel.searchPlaces(query)
        }
    }

    BackHandler {
        if (isLazyColumnVisible) {
            isLazyColumnVisible = false
        } else if (query.isNotEmpty()) {
            query = ""
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = query,
            onValueChange = {
                query = it
                if (query.length > 2) {
                    viewModel.searchPlaces(query)
                    isLazyColumnVisible = true
                }
            },
            label = { Text("Search for a place", color = Color.DarkGray )},
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                cursorColor = Color.DarkGray,
                focusedIndicatorColor = Color.DarkGray,
                unfocusedIndicatorColor = Color.DarkGray,
                backgroundColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = MaterialTheme.shapes.small)
                .border(1.dp, Color.White)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val places by viewModel.places.collectAsState()

        if (isLazyColumnVisible) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (query.isNotEmpty() && places.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .background(Color.White)
                            .padding(16.dp)
                            .zIndex(1f)
                    ) {
                        items(places) { place ->
                            PlaceItem(place) {
                                // Call the function to update weather info based on place's coordinates
                                weatherViewModel.loadWeatherInfo(
                                    latitude = place.lat,
                                    longitude = place.lon
                                )
                                isLazyColumnVisible = false // Hide LazyColumn after selection
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun PlaceItem(place: Place, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp) // Padding to space out items
    ) {
        // Place name
        Text(
            text = place.name,
            style = MaterialTheme.typography.h6,
            color = DarkBlue
        )
        // Country name
        Text(
            text = place.country,
            style = MaterialTheme.typography.body2,
            color = Color.Gray
        )
        // Separator line
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}