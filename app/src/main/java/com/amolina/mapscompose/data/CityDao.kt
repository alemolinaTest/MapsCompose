package com.amolina.mapscompose.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    fun getAllCities(): Flow<List<City>>

}