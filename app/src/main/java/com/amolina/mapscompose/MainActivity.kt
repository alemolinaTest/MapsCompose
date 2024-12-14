package com.amolina.mapscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.amolina.mapscompose.ui.AppScaffold
import com.amolina.mapscompose.ui.theme.MapsComposeTheme
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
                AppScaffold(mainViewModel)
            }
        }
    }
}
