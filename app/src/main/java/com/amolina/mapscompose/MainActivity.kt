package com.amolina.mapscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.amolina.mapscompose.ui.LocationPermissionScreen
import com.amolina.mapscompose.ui.theme.MapsComposeTheme
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
import android.Manifest
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import com.amolina.mapscompose.data.City
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @RequiresApi(35)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapsComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(mainViewModel)
                }
            }
        }
    }
}

@RequiresApi(35)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyApp(mainViewModel: MainViewModel) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
        // Show the map if permissions are granted
        GoogleMapScreen(mainViewModel)
    } else {
        // Request location permissions
        LocationPermissionScreen(locationPermissionsState)
    }

}

@RequiresApi(35)
@Composable
fun GoogleMapScreen(mainViewModel: MainViewModel) {
    // Define a position for the map camera
    val jsonCitiesState = mainViewModel.jsonCities.collectAsState()
//    val citiesState = mainViewModel.cities.collectAsState()
//    val city = City(id = 0, name = "San Francisco", country = "USA", lat = 37.7749, lon = -122.4194)
//    var defaultLocation = LatLng(37.7749, -122.4194) // San Francisco
//    defaultLocation = LatLng(city.lat, city.lon) // San Francisco
////    if(citiesState.value.first != null) {
////         defaultLocation = LatLng(citiesState.lat, citiesState.lon) // San Francisco
////    }
    val defaultLocation = LatLng(jsonCitiesState.value.first().coord.lat, jsonCitiesState.value.first().coord.lon)
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
            title = jsonCitiesState.value.first().name,
            snippet = "Welcome to ${jsonCitiesState.value.first().name} at ${jsonCitiesState.value.first().country}!"
        )
    }
}