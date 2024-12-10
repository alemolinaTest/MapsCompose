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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapsComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyApp() {
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
        // Show the map if permissions are granted
        GoogleMapScreen()
    } else {
        // Request location permissions
        LocationPermissionScreen(locationPermissionsState)
    }

}

@Composable
fun GoogleMapScreen() {
    // Define a position for the map camera
    val defaultLocation = LatLng(37.7749, -122.4194) // San Francisco
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
            title = "San Francisco",
            snippet = "Welcome to SF!"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGoogleMapScreen() {
    GoogleMapScreen()
}