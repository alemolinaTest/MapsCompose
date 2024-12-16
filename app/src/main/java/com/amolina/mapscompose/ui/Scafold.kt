package com.amolina.mapscompose.ui

import android.annotation.SuppressLint
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.amolina.mapscompose.MainViewModel
import com.amolina.mapscompose.navigation.AppNavigation
import kotlinx.coroutines.launch

@RequiresApi(35)
@SuppressLint("SuspiciousIndentation")
@Composable
fun AppScaffold(viewModel: MainViewModel) {

    var searchQuery by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    // Create and remember NavController
    val navController = rememberNavController()
    val cities by viewModel.jsonCities.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarWithSearch(searchQuery = searchQuery,
                onSearchQueryChange = { query ->
                    searchQuery = query
                    coroutineScope.launch {
                        viewModel.fetchFilteredJsonCities(query)
                    }
                },
                onShowAll = {
                    searchQuery = ""
                    coroutineScope.launch {
                        navController.currentBackStackEntry?.savedStateHandle?.set("cities", cities)
                        navController.navigate("googleMap")
                    }
                })
        },
    ) { innerPadding ->
        AppNavigation(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithSearch(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onShowAll: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { onSearchQueryChange(it) },
                    placeholder = { Text("Search tasks...") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    singleLine = true,
                )
                Button(
                    onClick = onShowAll,
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("Show All")
                }
            }
        }
    )
}
