package com.plcoding.weatherapp.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.presentation.components.LoadingScreen
import com.plcoding.weatherapp.presentation.components.PlacesSearch.PlacesViewModel
import com.plcoding.weatherapp.presentation.components.PlacesSearch.PlaceSearchScreen
import com.plcoding.weatherapp.presentation.components.WeatherCardAndForecast.WeatherCard
import com.plcoding.weatherapp.presentation.components.WeatherCardAndForecast.WeatherForecast
import com.plcoding.weatherapp.presentation.components.WeatherCardAndForecast.WeatherViewModel
import com.plcoding.weatherapp.presentation.ui.theme.DarkBlue
import com.plcoding.weatherapp.presentation.ui.theme.DeepBlue
import com.plcoding.weatherapp.presentation.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.loadWeatherInfo()
        }
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ))

        setContent {
            WeatherAppTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Weather App") },
                            backgroundColor = DarkBlue,
                            contentColor = Color.White
                        )
                    },
                    content = { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .background(DarkBlue)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                // PlaceSearchScreen at the top
                                PlaceSearchScreen(
                                    PlacesViewModel(),
                                    viewModel
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // WeatherCard below PlaceSearchScreen
                                WeatherCard(
                                    state = viewModel.state,
                                    backgroundColor = DeepBlue,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // WeatherForecast below WeatherCard
                                WeatherForecast(state = viewModel.state)
                            }

                            // Display custom LoadingScreen if loading
                            if (viewModel.state.isLoading) {
                                LoadingScreen()
                            }

                            // Display error message if there is an error
                            viewModel.state.error?.let { error ->
                                Text(
                                    text = error,
                                    color = Color.Red,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}
