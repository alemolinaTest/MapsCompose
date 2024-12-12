package com.amolina.mapscompose.navigation

import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.amolina.mapscompose.MainViewModel
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

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = "main") {
            composable("main") {
                MainScreen(
                    mainViewModel = viewModel,
                    modifier = modifier,
                    navController = navController
                )
            }
            composable("citiesList") {
                CitiesListScreen(
                    viewModel = viewModel,
                    modifier = modifier
                ) { cityId ->
                    viewModel.setLoading(true)
                    navController.navigate("googleMap/$cityId")
                }
            }
            composable(
                "googleMap/{cityId}",
                arguments = listOf(navArgument("cityId") { type = NavType.IntType })
            ) { backStackEntry ->
                val cityId = backStackEntry.arguments?.getInt("cityId")
                cityId?.let {
                    GoogleMapScreen(
                        cityId = it,
                        viewModel = viewModel,
                        onContentLoaded = { viewModel.setLoading(false) }
                    )
                }
            }
        }
        if (isLoading) {
            LoadingSpinner()
        }
    }
}