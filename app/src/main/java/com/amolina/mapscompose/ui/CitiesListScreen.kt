package com.amolina.mapscompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.amolina.mapscompose.MainViewModel
import com.amolina.mapscompose.models.LocalCity
import com.amolina.mapscompose.ui.theme.Purple40

@Composable
fun CitiesListScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val cities by viewModel.jsonCities.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(all = 16.dp)
    ) {
        items(cities) { city ->
            CityItem(city = city, navController = navController, viewModel = viewModel)
        }
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun CityItem(city: LocalCity, navController: NavHostController, viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(size = 8.dp)
            )
            .clickable {
                viewModel.setLoading(true)
                // Save the city object in the SavedStateHandle
                navController.currentBackStackEntry?.savedStateHandle?.set("city", city)
                navController.navigate("googleMap")
            }
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = city.name,
            color = Purple40,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = city.country,
            color = Color.Black,
            fontSize = 10.sp
        )
    }
}
