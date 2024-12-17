package com.amolina.mapscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.amolina.mapscompose.navigation.AppNavigation
import com.amolina.mapscompose.ui.MainScreen
import com.amolina.mapscompose.ui.theme.MapsComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(35)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MapsComposeTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                val viewModel: MainViewModel by viewModels()
                AppNavigation(navController = navController, viewModel = viewModel)
            }
        }
    }
}
