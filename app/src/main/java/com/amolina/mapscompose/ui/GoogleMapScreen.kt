package com.amolina.mapscompose.ui

import android.Manifest
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.amolina.mapscompose.MainViewModel
import com.amolina.mapscompose.models.LocalCity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@RequiresApi(35)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(mainViewModel: MainViewModel, navController: NavHostController) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
        // Show the cities list if permissions are granted
        CitiesListScreen(viewModel = mainViewModel, navController = navController)
    } else {
        // Request location permissions
        LocationPermissionScreen(locationPermissionsState)
    }

}

@RequiresApi(35)
@Composable
fun GoogleMapScreen(cities: List<LocalCity>, viewModel: MainViewModel) {

    // Simulate data loading
    LaunchedEffect(cities) {
        delay(1000) // Simulate loading
        viewModel.setLoading(false)
    }

    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        LoadingSpinner()
    } else {
        if (cities.isNotEmpty()) {
            val cameraPositionState = rememberCameraPositionState()
            // For multiple cities, calculate bounds and move the camera
            LaunchedEffect(cities) {
                if (cities.size == 1) {
                    // Single city: Set zoom level manually
                    val city = cities.first()
                    cameraPositionState.position = CameraPosition(
                        LatLng(city.coord.lat, city.coord.lon),
                        12f, // Adjust zoom for a single city
                        0f,
                        0f
                    )
                } else {
                    // Multiple cities: Use LatLngBounds
                    val boundsBuilder = LatLngBounds.Builder()
                    cities.forEach { city ->
                        boundsBuilder.include(LatLng(city.coord.lat, city.coord.lon))
                    }
                    val bounds = boundsBuilder.build()
                    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                    cameraPositionState.move(cameraUpdate)
                }
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = true,
                    mapType = MapType.NORMAL
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true
                ),
            ) {
                // Add markers for all cities
                cities.forEach { city ->
                    val markerPosition = LatLng(city.coord.lat, city.coord.lon)
                    Marker(
                        state = remember { MarkerState(position = markerPosition) },
                        title = city.name,
                        snippet = "Welcome to ${city.name} in ${city.country}!"
                    )
                }
            }
        } else {
            // Display a message if there are no cities
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "No cities available",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}