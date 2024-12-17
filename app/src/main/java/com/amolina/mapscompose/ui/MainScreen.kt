package com.amolina.mapscompose.ui

import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@RequiresApi(35)
@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
    ) {
        Button(
            onClick = { navController.navigate("scaffoldScreen") },
            modifier = Modifier.height(48.dp).padding(bottom = 10.dp)
        ) {
            Text("Maps Demo")
        }

        Button(
            onClick = { navController.navigate("exoplayer") },
            modifier = Modifier.height(48.dp).padding(bottom = 10.dp)
        ) {
            Text("Video Demo")
        }
    }
}