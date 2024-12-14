package com.amolina.mapscompose.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalCity(
    val id: Int,
    val name: String,
    val country: String,
    val coord: Coord
):Parcelable

@Parcelize
data class Coord(
    val lon: Double,
    val lat: Double
):Parcelable