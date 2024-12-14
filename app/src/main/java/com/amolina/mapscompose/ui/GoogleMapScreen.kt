package com.amolina.mapscompose.ui

import android.Manifest
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.amolina.mapscompose.MainViewModel
import com.amolina.mapscompose.models.LocalCity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
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
fun GoogleMapScreen(city: LocalCity, viewModel: MainViewModel) {
    // Define a position for the map camera
    // Simulate data loading
    LaunchedEffect(city) {
        // Load data here
        delay(1000)
        viewModel.setLoading(false)
    }

    val isloading = viewModel.isLoading.collectAsState()

    if (isloading.value) {
        LoadingSpinner()
    } else {

        city?.let {
            val defaultLocation =
                LatLng(city.coord.lat, city.coord.lon)
            val cameraPositionState = CameraPositionState(
                position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
            )
            val markerState = remember { MarkerState(defaultLocation) }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = true, // Enable user location
                    mapType = MapType.NORMAL // Options: NORMAL, HYBRID, SATELLITE, TERRAIN
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true
                ),
            ) {
                // Add a marker at the default location
                Marker(
                    state = markerState,
                    title = city.name,
                    snippet = "Welcome to ${city.name} at ${city.country}!"
                )
            }
        }
    }
}