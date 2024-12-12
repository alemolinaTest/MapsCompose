package com.amolina.mapscompose.models

import kotlinx.serialization.Serializable

@Serializable
data class LocalCity(
    val id: Int,
    val name: String,
    val country: String,
    val coord: Coord
)

@Serializable
data class Coord(
    val lon: Double,
    val lat: Double
)