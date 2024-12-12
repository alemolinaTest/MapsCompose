package com.amolina.mapscompose.repositories

import android.content.Context
import android.util.Log
import com.amolina.mapscompose.data.City
import com.amolina.mapscompose.data.CityDao
import com.amolina.mapscompose.models.LocalCity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

@Singleton
class CitiesRepository @Inject constructor(private val cityDao: CityDao, private val context: Context) {

    fun isFileAvailable(context: Context): Boolean {
        return try {
            context.assets.open("cities.json").close() // Just test file existence
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    fun loadCitiesFromAssets(context: Context): List<LocalCity> {
        if (isFileAvailable(context)) {
            Log.d("AssetsTest", "cities.json is available!")
        } else {
            Log.e("AssetsTest", "cities.json not found!")
        }
        // Read the JSON file from the assets folder
        val json = context.assets.open("cities.json").bufferedReader().use { it.readText() }

        // Parse the JSON into a list of LocalCity objects
        return Json.decodeFromString(json)
    }

    fun getJsonCities(): List<LocalCity> {
        val cities = loadCitiesFromAssets(context)
        // Perform additional transformations if needed
        cities.forEach { city ->
            println("City: ${city.name}, Country: ${city.country}, Coordinates: (${city.coord.lat}, ${city.coord.lon})")
        }
        return cities
    }

    fun getCities(): Flow<List<City>> = flow {
        cityDao.getAllCities()
    }
}