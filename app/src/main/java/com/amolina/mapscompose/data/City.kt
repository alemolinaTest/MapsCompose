package com.amolina.mapscompose.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amolina.mapscompose.data.City.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class City(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String,
    val lon: Double,
    val lat: Double
){
    companion object {
        const val TABLE_NAME = "cities"
    }
}