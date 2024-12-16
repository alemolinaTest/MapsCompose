package com.amolina.mapscompose.repositories

import android.content.Context
import com.amolina.mapscompose.data.City
import com.amolina.mapscompose.data.CityDao
import com.amolina.mapscompose.models.LocalCity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Singleton
class CitiesRepository @Inject constructor(private val cityDao: CityDao, private val context: Context) {

    private fun loadCitiesFromAssets(context: Context): List<LocalCity> {
        // Read the JSON file from the assets folder
        val json = context.assets.open("cities.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<LocalCity>>() {}.type
        // Parse the JSON into a list of LocalCity objects
        return Gson().fromJson(json, type)
    }

    fun getJsonCities(): List<LocalCity> {
        val cities = loadCitiesFromAssets(context)
        // Perform additional transformations if needed
        return cities.filter { it.country == "US" }
    }

    fun getCities(): Flow<List<City>> = flow {
        cityDao.getAllCities()
    }
}