package com.amolina.mapscompose.navigation

import VideoPlayerScreen
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amolina.mapscompose.MainViewModel
import com.amolina.mapscompose.models.LocalCity
import com.amolina.mapscompose.ui.CitiesListScreen
import com.amolina.mapscompose.ui.GoogleMapScreen
import com.amolina.mapscompose.ui.LoadingSpinner
import com.amolina.mapscompose.ui.MainScreen

@RequiresApi(35)
@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isLoading by viewModel.isLoading.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = "main") {
            composable("main") {
                MainScreen(
                    mainViewModel = viewModel,
                    navController = navController
                )
            }
            composable("citiesList") {
                CitiesListScreen(
                    viewModel = viewModel,
                    modifier = modifier,
                    navController = navController,
                )
            }
            composable(
                "googleMap",
            ) { _ ->
                val city = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<List<LocalCity>>("cities")
                city?.let { citiesList ->
                    GoogleMapScreen(
                        cities = citiesList,
                        viewModel = viewModel,
                    )
                }
            }
            composable("exoplayer") {
                VideoPlayerScreen()
            }
        }
        if (isLoading) {
            LoadingSpinner()
        }
    }
}